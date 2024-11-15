package com.example.todolist

import com.example.todolist.model.Task

interface SubtaskActionListener {

    fun renameSubtask(subtask: Task, newName: String)

    fun deleteSubtask(subtask: Task)
}