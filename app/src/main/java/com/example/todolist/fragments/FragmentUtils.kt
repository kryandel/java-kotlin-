package com.example.todolist.fragments

import androidx.fragment.app.Fragment
import com.example.todolist.App
import com.example.todolist.Navigator

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator