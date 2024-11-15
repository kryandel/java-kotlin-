package com.example.todolist.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.Collections
import kotlin.random.Random

typealias ListListener = (data : ListService.ListServiceData) -> Unit

class ListService(
    private val database: FirebaseFirestore
) {

    private val TAG = "LISTS"

    data class ListServiceData (
        var lists: MutableList<TaskList>,
        var selectedList: TaskList?
    )

    private var data = ListServiceData(
        lists = mutableListOf(),
        selectedList = null
    )

    private var listeners = mutableSetOf<ListListener>()

    private var dataLoaded = false

    private val favouriteIndex = 0

    init {
        database.collection(TAG).addSnapshotListener { result, error ->
            if (error != null) {
                Log.d(TAG, "LISTEN FAILED: $error")
                return@addSnapshotListener
            }

            if (result != null) {
                val lists = mutableListOf<TaskList>()
                result.forEach { doc ->
                    lists.add(doc.toObject())
                    Log.d(TAG, "(Update data) Read document: ${doc.id}")
                }
                if (lists.isNotEmpty()) {
                    data.lists = lists
                }
            }
            notifyChanges()
        }

        loadLists()
    }

    private fun loadLists() {
        data.lists.add(createFavouriteList())
        data.lists.add(createCreateList())
        data.selectedList = data.lists[0]

        loadFromFirestore()

        notifyChanges()
    }

    private fun loadFromFirestore() {
        val lists = mutableListOf<TaskList>()
        database.collection(TAG).get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (doc in it.result) {
                    lists.add(doc.toObject())
                    Log.d(TAG, "Read document: ${doc.id}")
                }
            }

            if (lists.isNotEmpty()) {
                data.lists = lists.sortedBy { list -> list.listType } as MutableList
                dataLoaded = true
                notifyChanges()
            } else {
                initializeLists()
            }
        }.addOnFailureListener {
            Log.d(TAG, "FAILED TO LOAD LISTS ${it.message}")
        }
    }

    private fun saveListOnDatabase(list: TaskList) {
        saveListOnFirestore(list)
    }

    private fun saveListOnFirestore(list: TaskList) {
        database.collection(TAG).add(list).addOnSuccessListener {
            Log.d(TAG, "Added favourite document: ${it.id}")
            notifyChanges()
        }
    }

    private fun deleteListFromDatabase(list: TaskList) {
        deleteListFromFirestore(list)
    }

    private fun deleteListFromFirestore(list: TaskList) {
        database.collection(TAG).add(list).addOnSuccessListener {
            Log.d(TAG, "Added favourite document: ${it.id}")
        }
        database.collection(TAG).whereEqualTo("id", list.id).get().addOnSuccessListener {
            it.forEach { doc ->
                database.collection(TAG).document(doc.id).delete().addOnSuccessListener {
                    Log.d(TAG, "SUCCESS DELETE DOCUMENT: ${doc.id}")
                    notifyChanges()
                }
            }
        }
    }

    private fun saveTaskOnDatabase(list: TaskList, task: Task) {
        saveTaskOnFirestore(list, task)
    }

    private fun saveTaskOnFirestore(list: TaskList, task: Task) {
        val newList = list.copy().apply {
            tasks.add(task)
        }
        database.collection(TAG).whereEqualTo("id", list.id).get().addOnSuccessListener {
            it.forEach { doc ->
                database.collection(TAG).document(doc.id).set(newList).addOnSuccessListener {
                    Log.d(TAG, "SUCCESS UPDATE DOCUMENT: ${doc.id}")
                    notifyChanges()
                }
            }
        }
    }

    private fun updateListOnDatabase(list: TaskList) {
        updateListOnFirestore(list)
    }

    private fun updateListOnFirestore(list: TaskList) {
        database.collection(TAG).whereEqualTo("id", list.id).get().addOnSuccessListener {
            it.forEach { doc ->
                database.collection(TAG).document(doc.id).set(list).addOnSuccessListener {
                    Log.d(TAG, "SUCCESS UPDATE DOCUMENT")
                    notifyChanges()
                }
            }
        }
    }

    private fun updateTaskOnDatabase(list: TaskList, task: Task) {
        updateTaskOnFirestore(list, task)
    }

    private fun updateTaskOnFirestore(list: TaskList, task: Task) {
        val newList = list.copy().apply {
            tasks[tasks.indexOfFirst { it.id == task.id }] = task
        }
        database.collection(TAG).whereEqualTo("id", list.id).get().addOnSuccessListener {
            it.forEach { doc ->
                database.collection(TAG).document(doc.id).set(newList).addOnSuccessListener {
                    Log.d(TAG, "SUCCESS UPDATE DOCUMENT")
                    notifyChanges()
                }
            }
        }
    }

    private fun initializeLists() {
        database
            .collection(TAG)
            .add(createFavouriteList()).addOnSuccessListener {
                Log.d(TAG, "Added favourite document: ${it.id}")
            }
        database
            .collection(TAG)
            .add(createCreateList()).addOnSuccessListener {
                Log.d(TAG, "Added create document: ${it.id}")
            }
    }

    private fun createFavouriteList() : TaskList {
        return TaskList(
            id = getNextId(),
            name = "Favourite",
            tasks = mutableListOf(),
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.FAVOURITE
        )
    }

    private fun createCreateList(): TaskList {
        return TaskList(
            id = getNextId(),
            name = "Создать список",
            tasks = mutableListOf(),
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.NEW_BUTTON
        )
    }

    fun getNextId() = Random.nextInt()

    fun addListener(listener : ListListener) {
        listeners.add(listener)
        if (dataLoaded) {
            listener.invoke(data)
        }
    }

    fun removeListener(listener: ListListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        if (dataLoaded) {
            listeners.forEach { it.invoke(data) }
        }
    }

    fun createList(list: TaskList) {
        //data.lists.add(task)
        saveListOnDatabase(list)
        Collections.swap(data.lists, data.lists.size - 1, data.lists.size - 2)
        notifyChanges()
    }

    fun selectList(list: TaskList) {
        data.selectedList = list
        notifyChanges()
    }

    fun getSelectedList() : Result<TaskList> {
        if (data.selectedList != null) {
            return Result.success(data.selectedList as TaskList)
        } else {
            return Result.failure(java.util.NoSuchElementException("No selected element"))
        }
    }

    fun isFavouriteList(list: TaskList): Boolean {
        return list == getFavouriteList()
    }

    fun getList(id: Int) : Result<TaskList> {
        val index = data.lists.indexOfFirst { it.id == id }
        if (index != -1) {
            return Result.success(data.lists[index])
        }

        return Result.failure(ArrayIndexOutOfBoundsException(
            "Not found TaskList with index %d".format(id)
        ))
    }

    fun getFavouriteList(): TaskList {
        return data.lists.first { it.listType == TaskList.ListType.FAVOURITE }
    }

    fun getListsCount() : Int {
        return data.lists.count()
    }

    fun getFirst(): TaskList = data.lists.first()

    fun getLast() : TaskList = data.lists.last()

    fun sortSelectedList(type: TaskList.SortType) {
        //todo
    }

    fun addTask(list: TaskList, task: Task) {
        val index = data.lists.indexOf(list)

        if (task.isFavourite) {
            //data.lists[favouriteIndex].addTask(task)
            saveTaskOnFirestore(getFavouriteList(), task)
        }

        //data.lists[index].addTask(task)
        saveTaskOnFirestore(list, task)

        //notifyChanges()
    }

    fun addSubTask(list: TaskList, task: Task, subtask: Task) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)

        if (subtask.isFavourite) {
            data.lists[favouriteIndex].addTask(subtask)
        }

        data.lists[listIndex].tasks[taskIndex].addSubtask(subtask)
        notifyChanges()
    }

    fun changeCompleteStatus(list: TaskList, task: Task, status: Boolean) {
        if (task.isSubtask) {
            return changeCompleteStatus(list, task.parentTask as Task, task, status)
        }
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)

        //data.lists[listIndex].tasks[taskIndex].isCompleted = status
        if (task.isFavourite) {
            updateTaskOnDatabase(getFavouriteList(), task.copy().apply { isCompleted = status })
        }
        updateTaskOnDatabase(list, task.copy().apply { isCompleted = status })
        //notifyChanges()
    }

    fun changeCompleteStatus(list: TaskList, task: Task, subtask: Task, status: Boolean) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)

        //data.lists[listIndex].tasks[taskIndex].getSubtask(subtask).getOrThrow().isCompleted = status
        if (task.isFavourite) {
            updateTaskOnDatabase(getFavouriteList(), subtask.copy().apply { isCompleted = status })
        }
        updateTaskOnDatabase(list, task.copy().apply { subtasks[subtasks.indexOf(subtask)].isCompleted = status })
        //todo если обновляется значение через favourite - в основном списке не обновляется
        //notifyChanges()
    }

    fun addToFavourite(list: TaskList, task: Task) {
        if (task.isSubtask) {
            addToFavourite(list, task.parentTask as Task, task)
        }
        val listIndex = data.lists.indexOf(list)
        if (listIndex == -1) {
            println("BAD LIST INDEX $listIndex")
            return
        }
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)
        if (taskIndex == -1) {
            println("BAD TASK INDEX $taskIndex")
            return
        }
        val oldStatus = data.lists[listIndex].tasks[taskIndex].isFavourite
        if (oldStatus) {
            return
        }

        val taskToSave = task.copy().apply { isFavourite = true }
        //data.lists[listIndex].tasks[taskIndex].isFavourite = true
        updateTaskOnDatabase(list, taskToSave)
        //data.lists[favouriteIndex].addTask(data.lists[listIndex].tasks[taskIndex])
        saveTaskOnDatabase(getFavouriteList(), taskToSave)
        //notifyChanges()
    }

    fun addToFavourite(list: TaskList, task: Task, subtask: Task) {
        val listIndex = data.lists.indexOf(list)
        if (listIndex == -1) {
            println("BAD LIST INDEX $listIndex")
            return
        }
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)
        if (taskIndex == -1) {
            println("BAD TASK INDEX $taskIndex")
            return
        }
        val subtaskIndex = data.lists[listIndex].tasks[taskIndex].subtasks.indexOf(subtask)
        if (subtaskIndex == -1) {
            println("BAD SUBTASK INDEX $subtaskIndex")
            return
        }
        val oldStatus = data.lists[listIndex].tasks[taskIndex].subtasks[subtaskIndex].isFavourite
        if (oldStatus) {
            return
        }

        //data.lists[listIndex].tasks[taskIndex].subtasks[subtaskIndex].isFavourite = true
        updateTaskOnDatabase(list, task.copy().apply { subtasks[subtasks.indexOf(subtask)].isFavourite = true })
        //data.lists[favouriteIndex].addTask(data.lists[listIndex].tasks[taskIndex].subtasks[subtaskIndex])
        saveTaskOnDatabase(getFavouriteList(), subtask.copy().apply { isFavourite = true })
        //notifyChanges()
    }

    fun deleteFromFavourite(task: Task) {
        val taskIndex = data.lists[favouriteIndex].tasks.indexOf(task)
        if (taskIndex == -1) {
            println("BAD TASK INDEX $taskIndex")
            return
        }
        //data.lists[favouriteIndex].tasks[taskIndex].isFavourite = false
        //todo бновить значение в основном списке. как?
        //data.lists[favouriteIndex].deleteTask(data.lists[favouriteIndex].tasks[taskIndex])
        updateListOnDatabase(getFavouriteList().copy().apply { tasks.remove(task) })
        //notifyChanges()
    }

    fun getTasksCount(list: TaskList) : Result<Int> {
        val index = data.lists.indexOf(list)
        if (index == -1) {
            return Result.failure(ArrayIndexOutOfBoundsException("Not found list"))
        }

        return Result.success(data.lists[index].tasks.size)
    }

    fun changeList(oldValue: TaskList, newValue: TaskList) {
        val index = data.lists.indexOf(oldValue)
        if (index == -1) {
            return
        }
        //data.lists[index] = newValue
        updateListOnDatabase(newValue)
        //notifyChanges()
    }

    fun deleteList(list: TaskList) {
        if (getList(list.id).isFailure) {
            return
        }

        list.tasks.forEach { it1 ->
            it1.subtasks.forEach { it2 ->
                if (it2.isFavourite) {
                    //data.lists[favouriteIndex].deleteTask(it2)
                    updateListOnDatabase(getFavouriteList().copy().apply { tasks.remove(it2) })
                }
            }

            if (it1.isFavourite) {
                //data.lists[favouriteIndex].deleteTask(it1)
                updateListOnDatabase(getFavouriteList().copy().apply { tasks.remove(it1) })
            }
        }

        //data.lists.remove(list)
        deleteListFromDatabase(list)
        //todo select new list
        //notifyChanges()
    }
}