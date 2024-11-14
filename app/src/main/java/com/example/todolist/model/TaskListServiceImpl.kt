package com.example.todolist.model

typealias TaskListListener = (lists : List<TaskList>) -> Unit

class TaskListServiceImpl : TaskListService {

    private var lists = mutableListOf<TaskList>()

    private var listeners = mutableSetOf<TaskListListener>()

    init {
        lists = (1..100).map { TaskList(
            id = it,
            name = it.toString(),
            tasks = mutableListOf<Task>()
        ) }.toMutableList()
    }

    fun addListener(listener : TaskListListener) {
        listeners.add(listener)
        listener.invoke(lists)
    }

    fun removeListener(listener: TaskListListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(lists) }
    }

    override fun createTaskList(task: TaskList) {
        lists.add(task)
        notifyChanges()
    }

    override fun getTaskList(id: Int) : Result<TaskList> {
        val index = lists.indexOfFirst { it.id == id }
        if (index != -1) {
            return Result.success(lists[index])
        }

        return Result.failure(ArrayIndexOutOfBoundsException(
            "Not found TaskList with index %d".format(id)
        ))
    }

    override fun changeTaskList(oldValue : TaskList, newValue: TaskList) {
        if (getTaskList(oldValue.id).isFailure) {
            return
        }

        lists[oldValue.id] = newValue
        notifyChanges()
    }

    override fun deleteTaskList(list: TaskList) {
        if (getTaskList(list.id).isFailure) {
            return
        }

        lists.remove(list)
        notifyChanges()
    }
}