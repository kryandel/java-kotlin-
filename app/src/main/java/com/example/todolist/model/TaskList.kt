package com.example.todolist.model

data class TaskList (
    val id: Int,
    var name: String,
    val tasks: MutableList<Task>,
    var sortType: SortType,
    var listType: ListType
) {
    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun getTask(task_id: Int) : Result<Task> {
        val index = tasks.indexOfFirst { it.id == task_id }
        if (index != -1) {
            return Result.success(tasks[index])
        }

        return Result.failure(ArrayIndexOutOfBoundsException(
            "Not found task with index %d in TaskList %d".format(task_id, id)
        ))
    }

    fun changeTask(task_id: Int, new_value: Task) {
        val task = getTask(task_id)
        if (task.isFailure) {
            return
        }

        tasks[task_id] = new_value
    }

    fun deleteTask(task_id: Int) {
        val task = getTask(task_id)
        if (task.isFailure) {
            return
        }

        tasks.removeAt(task_id)
    }

    enum class SortType {
        DEFAULT,
        DATE,
        FAVOURITE
    }

    enum class ListType {
        USER_LIST,
        FAVOURITE,
        NEW_BUTTON
    }
}