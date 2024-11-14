package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.model.TaskList
import com.example.todolist.model.ListListener
import com.example.todolist.model.ListServiceImpl

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : ListAdapter

    private val taskListService: ListServiceImpl
        get() = (applicationContext as App).taskListService

    private val taskListsListener: ListListener = {
        adapter.lists = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListAdapter(object : ListActionListener {
            override fun onListMove(list: TaskList, moveBy: Int) {
                //pass
            }

            override fun onListDelete(list: TaskList) {
                taskListService.deleteList(list)
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