package com.example.todolist.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.App
import com.example.todolist.MainActivity
import com.example.todolist.Navigator

class ViewModelFactory(
    private val app: MainActivity
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ListViewModel::class.java -> {
                ListViewModel(app.listService)
            }
            ListDetailsViewModel::class.java -> {
                ListDetailsViewModel(app.listService)
            }
            BottomPanelViewModel::class.java -> {
                BottomPanelViewModel(app.listService)
            }
            NewListViewModel::class.java -> {
                NewListViewModel(app.listService)
            }
            NewTaskViewModel::class.java -> {
                NewTaskViewModel(app.listService)
            }
            SelectedListViewModel::class.java -> {
                SelectedListViewModel(app.listService)
            }
            TaskDetailsViewModel::class.java -> {
                TaskDetailsViewModel(app.listService)
            }
            else -> {
                throw java.lang.IllegalStateException("Unknown view model class")
            }
        }

        return viewModel as T
    }

}