package com.example.todolist.fragments

import androidx.lifecycle.ViewModel
import com.example.todolist.model.ListService
import com.example.todolist.model.Task
import java.time.LocalDate

class NewTaskViewModel(
    private val listService: ListService
) : ViewModel() {

    var name: String = String()
    var description: String = String()
    var date: Long = LocalDate.now().toEpochDay()
    var favourite: Boolean = false

    fun createTask() {
        listService.addTask(
            listService.getSelectedList().getOrThrow(), Task(
            id = listService.getTasksCount(listService.getSelectedList().getOrThrow()).getOrThrow(),
            name = name,
            date = date,
            description = description,
            isFavourite = favourite,
            isComplited = false,
            subtasks = mutableListOf()
        )
        )
    }
}