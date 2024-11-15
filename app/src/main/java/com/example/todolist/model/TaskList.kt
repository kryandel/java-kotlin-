package com.example.todolist.model

data class TaskList (
    val id: Int = -1,
    var name: String = "",
    val tasks: MutableList<Task> = mutableListOf(),
    var sortType: SortType = SortType.DEFAULT,
    var listType: ListType = ListType.USER_LIST
) {

    fun toMap() = hashMapOf(
        "id" to id,
        "name" to name,
        "tasks" to tasks,
        "sortType" to sortType,
        "listType" to listType
    )

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

    fun deleteTask(task: Task) {
        val taskId = tasks.indexOfFirst { it.id == task.id }
        if (taskId == -1) {
            return
        }

        tasks.removeAt(taskId)
    }

    enum class SortType {
        DEFAULT,
        DATE,
        FAVOURITE
    }

    enum class ListType {
        FAVOURITE,
        USER_LIST,
        NEW_BUTTON
    }
}