package com.example.todolist.model

import com.google.firebase.firestore.FirebaseFirestore
import java.util.Collections

typealias ListListener = (data : ListService.ListServiceData) -> Unit

class ListService(
    private val database: FirebaseFirestore
) {

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
        loadLists()
    }

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

    fun createList(task: TaskList) {
        data.lists.add(task)
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
        return list == data.lists[favouriteIndex]
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

    fun getLists() : List<TaskList> {
        return data.lists
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
            data.lists[favouriteIndex].addTask(task)
        }

        data.lists[index].addTask(task)
        notifyChanges()
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

        data.lists[listIndex].tasks[taskIndex].isCompleted = status
        notifyChanges()
    }

    fun changeCompleteStatus(list: TaskList, task: Task, subtask: Task, status: Boolean) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)

        data.lists[listIndex].tasks[taskIndex].getSubtask(subtask).getOrThrow().isCompleted = status
        notifyChanges()
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

        data.lists[listIndex].tasks[taskIndex].isFavourite = true
        data.lists[favouriteIndex].addTask(data.lists[listIndex].tasks[taskIndex])
        notifyChanges()
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

        data.lists[listIndex].tasks[taskIndex].subtasks[subtaskIndex].isFavourite = true
        data.lists[favouriteIndex].addTask(data.lists[listIndex].tasks[taskIndex].subtasks[subtaskIndex])
        notifyChanges()
    }

    fun deleteFromFevourite(task: Task) {
        val taskIndex = data.lists[favouriteIndex].tasks.indexOf(task)
        if (taskIndex == -1) {
            println("BAD TASK INDEX $taskIndex")
            return
        }
        data.lists[favouriteIndex].tasks[taskIndex].isFavourite = false
        data.lists[favouriteIndex].deleteTask(data.lists[favouriteIndex].tasks[taskIndex])
        notifyChanges()
    }

    fun getTasksCount(list: TaskList) : Result<Int> {
        val index = data.lists.indexOf(list)
        if (index == -1) {
            return Result.failure(ArrayIndexOutOfBoundsException("Not found list"))
        }

        return Result.success(data.lists[index].tasks.size)
    }

    fun loadLists(): List<TaskList> {
        data.lists.add(TaskList(
            id = 0,
            name = "Favourite",
            tasks = mutableListOf(),
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.FAVOURITE
        ))

        data.lists.add(TaskList(
            id = 1,
            name = "Создать список",
            tasks = mutableListOf(),
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.NEW_BUTTON
        ))

        dataLoaded = true
        data.selectedList = data.lists[0]
        notifyChanges()
        return data.lists
    }

    fun changeList(oldValue: TaskList, newValue: TaskList) {
        val index = data.lists.indexOf(oldValue)
        if (index == -1) {
            return
        }
        data.lists[index] = newValue
        notifyChanges()
    }

    fun deleteList(list: TaskList) {
        if (getList(list.id).isFailure) {
            return
        }

        list.tasks.forEach { it1 ->
            it1.subtasks.forEach { it2 ->
                if (it2.isFavourite) {
                    data.lists[favouriteIndex].deleteTask(it2)
                }
            }

            if (it1.isFavourite) {
                data.lists[favouriteIndex].deleteTask(it1)
            }
        }

        data.lists.remove(list)
        notifyChanges()
    }
}