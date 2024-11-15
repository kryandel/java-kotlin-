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
    fun toMap() = hashMapOf(
        "id" to id,
        "name" to name,
        "date" to date,
        "description" to description,
        "isFavourite" to isFavourite,
        "isCompleted" to isCompleted,
        "isSubtask" to isSubtask,
        "parentTask" to parentTask,
        "subtasks" to subtasks
    )

    fun addSubtask(subtask: Task) {
        subtasks.add(subtask)
    }

    fun getSubtask(subtask: Task) : Result<Task> {
        val index = subtasks.indexOf(subtask)
        if (index == -1) {
            return Result.failure(ArrayIndexOutOfBoundsException())
        }

        return Result.success(subtasks[index])
    }

    fun changeSubtask(subtask_id: Int, new_value: Task) {
        subtasks[subtask_id] = new_value
    }

    fun deleteSubtask(subtask: Task) {
        subtasks.remove(subtask)
    }
}
