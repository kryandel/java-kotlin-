package com.example.todolist

import com.example.todolist.model.TaskList

interface TaskListActionListener {

    fun onListMove(list: TaskList, moveBy: Int)

    fun onListDelete(list: TaskList)

    fun onListDetails(list: TaskList)

}