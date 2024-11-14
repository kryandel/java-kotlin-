package com.example.todolist.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.model.ListListener
import com.example.todolist.model.ListService
import com.example.todolist.model.TaskList

class ListViewModel(
    private val listService: ListService
) : ViewModel() {

    private val _lists = MutableLiveData< List<TaskList> >()
    val lists: LiveData< List<TaskList> > = _lists

    init {
        loadLists()
    }

    fun loadLists() {
        listService.addListener(listener)
    }

    fun moveList(list: TaskList, moveBy: Int) {
        listService.moveList(list, moveBy)
    }

    fun deleteList(list: TaskList) {
        listService.deleteList(list)
    }

    private val listener: ListListener = {
        _lists.value = it
    }

    override fun onCleared() {
        super.onCleared()
        listService.removeListener(listener)
    }
}