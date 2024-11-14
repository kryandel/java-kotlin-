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

class SelectedListViewModel(
    private val listService: ListService
) : ViewModel() {

    private var _tasks = MutableLiveData< List<Task> >()
    val tasks: LiveData< List<Task> > = _tasks

    private val listener: ListListener = {
        if (it.selectedList != null) {
            _tasks.value = (it.selectedList as TaskList).tasks
        }
    }

    init {
        listService.addListener(listener)
        viewModelScope.launch { loadSelectedList() }
    }

    fun loadSelectedList() = viewModelScope.launch {
        val selectedList = listService.getSelectedList().getOrNull()
        if (selectedList != null) {
            _tasks.value = selectedList.tasks
        }
    }

    override fun onCleared() {
        super.onCleared()
        listService.removeListener(listener)
    }
}