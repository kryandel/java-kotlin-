package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.fragments.*
import com.example.todolist.model.TaskList
import com.example.todolist.model.Task

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, ListFragment())
                .add(R.id.fragmentContainer, BottomPanelFragment())
                .commit()
        }
    }

    override fun showTaskDetails(task: Task) {
        //TODO("Not yet implemented")
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