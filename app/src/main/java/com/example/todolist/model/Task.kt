package com.example.todolist.model

data class Task (
    val id: Int = -1,
    var name: String = "",
    var date: Long = 0,
    var description : String = "",
    var isFavourite: Boolean = false,
    var isCompleted: Boolean = false,
    var isSubtask: Boolean = false,
    var parentTask: Task? = null,
    val subtasks: MutableList<Task> = mutableListOf(),
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
