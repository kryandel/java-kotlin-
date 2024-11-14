package com.example.todolist

import android.app.Application
import com.example.todolist.model.TaskListServiceImpl

class App : Application() {

    val taskListService = TaskListServiceImpl()
}