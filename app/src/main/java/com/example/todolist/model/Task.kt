package com.example.todolist.model

import java.sql.Date

data class Task (
    val id: Int,
    val name: String,
    val date: Date,
    val description : String,
    val isFavourite: Boolean,
    val subtasks: MutableList<Task>
) {
    fun addSubTask(task: Task) {
        subtasks.add(task)
    }

    fun getSubTask(task_id: Int) : Result<Task> {
        val index = subtasks.indexOfFirst { it.id == task_id }
        if (index != -1) {
            return Result.success(subtasks[index])
        }
        return Result.failure(ArrayIndexOutOfBoundsException(
            "Not found subtask with index %d in task %d".format(task_id, id)
        ))
    }

    fun changeSubTask(task_id: Int, new_value: Task) {
        val task = getSubTask(task_id)
        if (task.isFailure) {
            return
        }

        subtasks[task_id] = new_value
    }

    fun deleteSubTask(task_id: Int) {
        val task = getSubTask(task_id)
        if (task.isFailure) {
            return
        }

        subtasks.removeAt(task_id)
    }
}
