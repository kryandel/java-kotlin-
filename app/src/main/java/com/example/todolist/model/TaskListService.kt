package com.example.todolist.model

interface TaskListService {
    fun CreateTaskList(task: TaskList)
    fun GetTaskList(id: Long)
    fun ChangeTaskList(id: Long, newValue: TaskList)
    fun DeleteTaskList(id: Long)
}