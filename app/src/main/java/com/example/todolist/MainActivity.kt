package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.ItemListBinding
import com.example.todolist.model.TaskList
import com.example.todolist.model.TaskListListener
import com.example.todolist.model.TaskListService
import com.example.todolist.model.TaskListServiceImpl

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : TaskListAdapter

    private val taskListService: TaskListServiceImpl
        get() = (applicationContext as App).taskListService

    private val taskListsListener: TaskListListener = {
        adapter.lists = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskListAdapter(object : TaskListActionListener {
            override fun onListMove(list: TaskList, moveBy: Int) {
                //pass
            }

            override fun onListDelete(list: TaskList) {
                taskListService.deleteTaskList(list)
            }

            override fun onListDetails(list: TaskList) {
                //pass
            }

        })

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.listsRecyclerView.layoutManager = layoutManager
        binding.listsRecyclerView.adapter = adapter

        taskListService.addListener(taskListsListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        taskListService.removeListener(taskListsListener)
    }
}