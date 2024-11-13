package com.example.todolist.model

interface TaskService {
    fun CreateTask(task: Task)
    fun GetTask(id: Long)
    fun ChangeTask(id: Long, newValue: Task)
    fun DeleteTask(id: Long)
}