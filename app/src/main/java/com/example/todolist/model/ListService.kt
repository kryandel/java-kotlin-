package com.example.todolist.model

import java.util.Collections

typealias ListListener = (data : ListService.ListServiceData) -> Unit

class ListService {

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

        data.lists[listIndex].tasks[taskIndex].addSubTask(subtask)
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

        data.lists[listIndex].tasks[taskIndex].getSubTask(subtask).getOrThrow().isCompleted = status
        notifyChanges()
    }

    fun changeFavouriteStatus(list: TaskList, task: Task, status: Boolean) {
        if (task.isSubtask) {
            return changeFavouriteStatus(list, task.parentTask as Task, task, status)
        }
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)
        val oldStatus = data.lists[listIndex].tasks[taskIndex].isFavourite

        data.lists[listIndex].tasks[taskIndex].isFavourite = status

        if (oldStatus && !status) {
            data.lists[favouriteIndex].deleteTask(data.lists[listIndex].tasks[taskIndex])
        } else if (!oldStatus && status) {
            data.lists[favouriteIndex].addTask(data.lists[listIndex].tasks[taskIndex])
        }

        notifyChanges()
    }

    fun changeFavouriteStatus(list: TaskList, task: Task, subtask: Task, status: Boolean) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)
        val oldStatus = data.lists[listIndex].tasks[taskIndex].getSubTask(subtask).getOrThrow().isFavourite

        data.lists[listIndex].tasks[taskIndex].getSubTask(subtask).getOrThrow().isFavourite = status
        if (oldStatus && !status) {
            data.lists[favouriteIndex].deleteTask(
                data.lists[listIndex].tasks[taskIndex].getSubTask(subtask).getOrThrow()
            )
        } else if (!oldStatus && status) {
            data.lists[favouriteIndex].addTask(
                data.lists[listIndex].tasks[taskIndex].getSubTask(subtask).getOrThrow()
            )
        }

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
        val tasks = mutableListOf<Task>()
        tasks.add(Task(
            id = 10,
            name = "dffdf",
            date = 0,
            description = "",
            isFavourite = false,
            isCompleted = false,
            isSubtask = false,
            parentTask = null,
            subtasks = mutableListOf()
        ))
        tasks[0].subtasks.add(Task(
            id = 100,
            name = "sub",
            date = 1,
            description = "",
            isFavourite = false,
            isCompleted = false,
            isSubtask = true,
            parentTask = tasks[0],
            subtasks = mutableListOf()
        ))
        data.lists.add(TaskList(
            id = 2,
            name = "def",
            tasks = tasks,
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.USER_LIST
        ))
        data.lists.add(TaskList(
            id = 1,
            name = "Создать список",
            tasks = mutableListOf(),
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.NEW_BUTTON
        ))
        /*data.lists = (1..9).map { TaskList(
            id = it,
            name = it.toString(),
            tasks = mutableListOf(
                Task(
                    id = 0,
                    name = "default task" + it.toString(),
                    date = 0,
                    description = "",
                    isFavourite = (it % 2) == 1,
                    isCompleted = (it % 2) == 0,
                    isSubtask = false,
                    subtasks = mutableListOf()
                )),
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.USER_LIST
        ) }.toMutableList()

        data.lists.add(TaskList(
            id = 10,
            name = "Создать список",
            tasks = mutableListOf(),
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.NEW_BUTTON
        ))*/

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