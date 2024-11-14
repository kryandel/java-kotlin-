package com.example.todolist

import com.example.todolist.model.Task
import com.example.todolist.model.TaskList

interface Navigator {

    fun showTaskDetails(task: Task)

    fun showListDetails(list: TaskList)

    fun goBack()

    fun toast(messageRes: Int)

}