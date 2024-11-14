package com.example.todolist.model

interface TaskListService {

    fun createTaskList(task: TaskList)
    fun getTaskList(id: Int) : Result<TaskList>
    fun changeTaskList(oldValue : TaskList, newValue: TaskList)
    fun deleteTaskList(list : TaskList)
}