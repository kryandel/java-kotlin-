package com.example.todolist.model

interface TaskListService {

    fun createTaskList(task: TaskList)
    fun getTaskList(id: Int) : Result<TaskList>
    fun changeTaskList(id: Int, newValue: TaskList)
    fun deleteTaskList(id: Int)
}