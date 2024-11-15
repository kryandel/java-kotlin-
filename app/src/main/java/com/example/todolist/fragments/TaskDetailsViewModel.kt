package com.example.todolist.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.model.ListService
import com.example.todolist.model.Task

class TaskDetailsViewModel(
    private val listService: ListService
) : ViewModel() {

    private val _subtask = MutableLiveData< Task? >()
    private val listId = MutableLiveData< Int? >()
    private val taskId = MutableLiveData< Int? >()
    val subtask: LiveData< Task? > = _subtask

    fun loadSubtasks(idList: Int, idTask: Int) {
        if (subtask.value != null) {
            return
        }

        listId.value = idList
        taskId.value = idTask

        _subtask.value = listService.getList(idList).getOrNull()?.getTask(idTask)?.getOrNull()
    }

    fun renameSubtask(subtask: Task, newName: String) {
        if (_subtask.value == null) {
            return
        }

        subtask.name = newName
        listService.getList(listId.value as Int).getOrThrow().getTask(taskId.value as Int)
            .getOrThrow()
            .changeSubtask(subtask.id, subtask)
    }

    fun addSubtask(name: String) {
        if (_subtask.value == null) {
            return
        }

        val subtask = Task (
            id = 1000, //todo random id
            name = name,
            date = 0,
            description = "",
            isFavourite = false,
            isCompleted = false,
            isSubtask = true,
            parentTask = listService.getList(listId.value as Int).getOrThrow()
                .getTask(taskId.value as Int)
                .getOrThrow(),
            subtasks = mutableListOf()
        )

        listService.getList(listId.value as Int).getOrThrow().getTask(taskId.value as Int)
            .getOrThrow()
            .addSubtask(subtask)
    }

    fun deleteSubtask(subtask: Task) {
        if (_subtask.value == null) {
            return
        }

        listService.getList(listId.value as Int).getOrThrow().getTask(taskId.value as Int)
            .getOrThrow()
            .deleteSubtask(subtask)
    }
}