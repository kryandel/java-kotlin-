package com.example.todolist.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.ListListener
import com.example.todolist.model.ListService
import com.example.todolist.model.TaskList
import kotlinx.coroutines.launch

class ListViewModel(
    private val listService: ListService
) : ViewModel() {

    private val _lists = MutableLiveData< List<TaskList> >()
    val lists: LiveData< List<TaskList> > = _lists

    private val listener: ListListener = {
        _lists.value = it
    }

    init {
        listService.addListener(listener)
        viewModelScope.launch { loadLists() }
    }

    fun loadLists() =  viewModelScope.launch {
    }

    fun moveList(list: TaskList, moveBy: Int) =  viewModelScope.launch {
        listService.moveList(list, moveBy)
    }

    fun deleteList(list: TaskList) =  viewModelScope.launch {
        listService.deleteList(list)
    }

    override fun onCleared() {
        super.onCleared()
        listService.removeListener(listener)
    }
}