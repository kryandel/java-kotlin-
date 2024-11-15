package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.fragments.*
import com.example.todolist.model.ListService
import com.example.todolist.model.TaskList
import com.example.todolist.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding : ActivityMainBinding

    private val database = Firebase.firestore
    val listService = ListService(database)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, ListFragment())
                .add(R.id.fragmentContainer, SelectedListFragment())
                .add(R.id.fragmentContainer, BottomPanelFragment())
                .commit()
        }
    }

    override fun showTaskDetails(list: TaskList, task: Task) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, TaskDetailsFragment.new(list.id, task.id, null))
            .commit()
    }

    override fun showSubtaskDetails(list: TaskList, task: Task, subtask: Task) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, TaskDetailsFragment.new(list.id, task.id, subtask.id))
            .commit()
    }

    override fun showListDetails(list: TaskList) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, ListDetailsFragment.new(list.id))
            .commit()
    }

    override fun showNewList() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, NewListFragment())
            .commit()
    }

    override fun showNewTask() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, NewTaskFragment())
            .commit()
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

}