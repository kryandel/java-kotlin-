package com.example.todolist.fragments

import androidx.fragment.app.Fragment
import com.example.todolist.App
import com.example.todolist.MainActivity
import com.example.todolist.Navigator

fun Fragment.factory() = ViewModelFactory(requireActivity() as MainActivity)

fun Fragment.navigator() = requireActivity() as Navigator