package com.example.todolist.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.model.ListService
import com.example.todolist.model.TaskList

class ListDetailsViewModel(
    private val listService: ListService
) : ViewModel() {

    private val _listDetails = MutableLiveData< TaskList >()
    val listDetails: LiveData< TaskList > = _listDetails

    fun loadList(id: Int) {
        if (_listDetails.value != null) {
            return
        }
        _listDetails.value = listService.getList(id).getOrNull()
    }

    fun sortList() {

    }

    fun renameList(newName: String) {
        val list = this.listDetails.value ?: return
        var newList = list.apply { name = newName }
        listService.changeList(list, newList)
    }

    fun deleteList() {
        val list = this.listDetails.value ?: return
        listService.deleteList(list)
    }

    fun getFirst(): TaskList = listService.getFirst()

    fun getLast(): TaskList = listService.getLast()

    fun deleteCompletedTasks() {

    }
}