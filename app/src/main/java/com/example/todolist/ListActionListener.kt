package com.example.todolist

import com.example.todolist.model.TaskList

interface ListActionListener {

    fun selectList(list: TaskList)

}