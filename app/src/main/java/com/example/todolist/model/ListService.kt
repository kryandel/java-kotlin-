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

    fun loadLists(): List<TaskList> {
        data.lists = (1..100).map { TaskList(
            id = it,
            name = it.toString(),
            tasks = mutableListOf<Task>(),
            sortType = TaskList.SortType.DEFAULT
        ) }.toMutableList()

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
    }

    fun deleteList(list: TaskList) {
        if (getList(list.id).isFailure) {
            return
        }

        data.lists.remove(list)
        notifyChanges()
    }
}