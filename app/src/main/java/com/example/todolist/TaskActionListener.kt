package com.example.todolist

import com.example.todolist.model.Task

interface TaskActionListener {

    fun changeFavouriteStatus(task: Task, status: Boolean)

    fun changeCompleteStatus(task: Task, status: Boolean)

    fun showDetails(task: Task)

}