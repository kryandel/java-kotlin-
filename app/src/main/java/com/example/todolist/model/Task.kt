package com.example.todolist.model

data class Task (
    val id: Int,
    var name: String,
    var date: Long,
    var description : String,
    var isFavourite: Boolean,
    var isCompleted: Boolean,
    var isSubtask: Boolean,
    var parentTask: Task?,
    val subtasks: MutableList<Task>,
) {
    fun addSubTask(task: Task) {
        subtasks.add(task)
    }

    fun getSubTask(subtask: Task) : Result<Task> {
        val index = subtasks.indexOf(subtask)
        if (index == -1) {
            return Result.failure(ArrayIndexOutOfBoundsException())
        }

        return Result.success(subtasks[index])
    }

    fun changeSubTask(task_id: Int, new_value: Task) {
        //todo?
    }

    fun deleteSubTask(task: Task) {
        //todo?
    }
}
