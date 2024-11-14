package com.example.todolist.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.App

class ViewModelFactory(
    private val app: App
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ListViewModel::class.java -> {
                ListViewModel(app.taskListService)
            }
            else -> {
                throw java.lang.IllegalStateException("Unknown view model class")
            }
        }

        return viewModel as T
    }

}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)