package com.example.todolist

import android.app.Application
import com.example.todolist.model.ListServiceImpl

class App : Application() {

    val taskListService = ListServiceImpl()
}