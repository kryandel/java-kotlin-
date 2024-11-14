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

    fun addTask(list: TaskList, task: Task) {
        val index = data.lists.indexOf(list)

        data.lists[index].addTask(task)
        notifyChanges()
    }

    fun addSubTask(list: TaskList, task: Task, subtask: Task) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)

        data.lists[listIndex].tasks[taskIndex].subtasks.add(subtask)
        notifyChanges()
    }

    fun changeCompleteStatus(list: TaskList, task: Task, status: Boolean) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)

        data.lists[listIndex].tasks[taskIndex].isComplited = status
        notifyChanges()
    }

    fun changeCompleteStatus(list: TaskList, task: Task, subtask: Task, status: Boolean) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)
        val subtaskIndex = data.lists[listIndex].tasks[taskIndex].subtasks.indexOf(subtask)

        data.lists[listIndex].tasks[taskIndex].subtasks[subtaskIndex].isComplited = status
        notifyChanges()
    }

    fun changeFavouriteStatus(list: TaskList, task: Task, status: Boolean) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)

        data.lists[listIndex].tasks[taskIndex].isFavourite = status
        notifyChanges()
    }

    fun changeFavouriteStatus(list: TaskList, task: Task, subtask: Task, status: Boolean) {
        val listIndex = data.lists.indexOf(list)
        val taskIndex = data.lists[listIndex].tasks.indexOf(task)
        val subtaskIndex = data.lists[listIndex].tasks[taskIndex].subtasks.indexOf(subtask)

        data.lists[listIndex].tasks[taskIndex].subtasks[subtaskIndex].isFavourite = status
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
        data.lists = (1..9).map { TaskList(
            id = it,
            name = it.toString(),
            tasks = mutableListOf(
                Task(
                    id = 0,
                    name = "default task" + it.toString(),
                    date = 0,
                    description = "",
                    isFavourite = (it % 2) == 1,
                    isComplited = (it % 2) == 0,
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
        ))

        dataLoaded = true
        data.selectedList = data.lists[0]
        notifyChanges()
        return data.lists
    }

    fun moveList(list: TaskList, moveBy: Int) {
        if (getList(list.id).isFailure) {
            return
        }

        val index = data.lists.indexOf(list)

        Collections.swap(data.lists, index, index + moveBy)
        notifyChanges()
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

        data.lists.remove(list)
        notifyChanges()
    }
}