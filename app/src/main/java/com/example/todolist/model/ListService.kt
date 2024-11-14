package com.example.todolist.model

import java.util.Collections

typealias ListListener = (lists : List<TaskList>) -> Unit

class ListService {

    private var lists = mutableListOf<TaskList>()

    private var listeners = mutableSetOf<ListListener>()

    private var dataLoaded = false

    init {
        loadLists()
    }

    fun addListener(listener : ListListener) {
        listeners.add(listener)
        if (dataLoaded) {
            listener.invoke(lists)
        }
    }

    fun removeListener(listener: ListListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        if (dataLoaded) {
            listeners.forEach { it.invoke(lists) }
        }
    }

    fun createList(task: TaskList) {
        lists.add(task)
        notifyChanges()
    }

    fun getList(id: Int) : Result<TaskList> {
        val index = lists.indexOfFirst { it.id == id }
        if (index != -1) {
            return Result.success(lists[index])
        }

        return Result.failure(ArrayIndexOutOfBoundsException(
            "Not found TaskList with index %d".format(id)
        ))
    }

    fun loadLists(): List<TaskList> {
        lists = (1..100).map { TaskList(
            id = it,
            name = it.toString(),
            tasks = mutableListOf<Task>(),
            sortType = TaskList.SortType.DEFAULT
        ) }.toMutableList()

        dataLoaded = true
        notifyChanges()
        return lists
    }

    fun moveList(list: TaskList, moveBy: Int) {
        if (getList(list.id).isFailure) {
            return
        }

        val index = lists.indexOf(list)

        Collections.swap(lists, index, index + moveBy)
        notifyChanges()
    }

    fun changeList(oldValue: TaskList, newValue: TaskList) {
        val index = lists.indexOf(oldValue)
        if (index == -1) {
            return
        }
        lists[index] = newValue
    }

    fun deleteList(list: TaskList) {
        if (getList(list.id).isFailure) {
            return
        }

        lists.remove(list)
        notifyChanges()
    }
}