package com.example.todolist.model

import java.util.Collections

typealias ListListener = (lists : List<TaskList>) -> Unit

class ListServiceImpl : ListService {

    private var lists = mutableListOf<TaskList>()

    private var listeners = mutableSetOf<ListListener>()

    init {
        lists = (1..100).map { TaskList(
            id = it,
            name = it.toString(),
            tasks = mutableListOf<Task>()
        ) }.toMutableList()
    }

    override fun addListener(listener : ListListener) {
        listeners.add(listener)
        listener.invoke(lists)
    }

    override fun removeListener(listener: ListListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(lists) }
    }

    override fun createList(task: TaskList) {
        lists.add(task)
        notifyChanges()
    }

    override fun getList(id: Int) : Result<TaskList> {
        val index = lists.indexOfFirst { it.id == id }
        if (index != -1) {
            return Result.success(lists[index])
        }

        return Result.failure(ArrayIndexOutOfBoundsException(
            "Not found TaskList with index %d".format(id)
        ))
    }

    override fun moveList(list: TaskList, moveBy: Int) {
        if (getList(list.id).isFailure) {
            return
        }

        val index = lists.indexOf(list)

        Collections.swap(lists, index, index + moveBy)
        notifyChanges()
    }

    override fun deleteList(list: TaskList) {
        if (getList(list.id).isFailure) {
            return
        }

        lists.remove(list)
        notifyChanges()
    }
}