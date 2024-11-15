package com.example.todolist.fragments

import androidx.lifecycle.ViewModel
import com.example.todolist.model.ListService
import com.example.todolist.model.TaskList

class NewListViewModel(
    private val listService: ListService
) : ViewModel() {

    var name: String = String()

    fun createList() {
        if (name.isEmpty()) {
            return
        }

        val list = TaskList(
            id = listService.getListsCount(),
            name = name,
            tasks = mutableListOf(),
            sortType = TaskList.SortType.DEFAULT,
            listType = TaskList.ListType.USER_LIST
        )

        listService.createList(list)
        listService.selectList(list)
    }
}