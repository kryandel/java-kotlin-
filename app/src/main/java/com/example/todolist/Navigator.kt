package com.example.todolist

import com.example.todolist.model.Task
import com.example.todolist.model.TaskList

interface Navigator {

    fun showTaskDetails(list: TaskList, task: Task)

    fun showSubtaskDetails(list: TaskList, task: Task, subtask: Task)

    fun showListDetails(list: TaskList)

    fun showNewList()

    fun showNewTask()

    fun goBack()

}