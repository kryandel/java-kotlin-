package com.example.todolist.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.ListListener
import com.example.todolist.model.ListService
import com.example.todolist.model.Task
import com.example.todolist.model.TaskList
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate

class BottomPanelViewModel(
    private val listService: ListService
) : ViewModel() {

    private val _list = MutableLiveData< TaskList? >()
    val list: LiveData< TaskList? > = _list

    private val listener: ListListener = {
        _list.value = it.selectedList
    }

    init {
        _list.value = listService.getSelectedList().getOrNull()
        listService.addListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        listService.removeListener(listener)
    }
}