package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.TaskActionListener
import com.example.todolist.TaskAdapter
import com.example.todolist.databinding.FragmentSelectedListBinding
import com.example.todolist.model.Task
import com.example.todolist.model.TaskList

class SelectedListFragment : Fragment() {

    private lateinit var binding: FragmentSelectedListBinding
    private lateinit var adapter: TaskAdapter

    private val viewModel: SelectedListViewModel by viewModels{ factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectedListBinding.inflate(inflater, container, false)
        adapter = TaskAdapter(object : TaskActionListener {
            override fun changeFavouriteStatus(task: Task, status: Boolean) {
                viewModel.changeFavouriteStatus(task, status)
            }

            override fun changeCompleteStatus(task: Task, status: Boolean) {
                viewModel.changeCompleteStatus(task, status)
            }

            override fun showDetails(task: Task) {
                if (viewModel.getList() != null) {
                    if (task.isSubtask) {
                        navigator().showSubtaskDetails(viewModel.getList() as TaskList, task, task.parentTask as Task)
                    } else {
                        navigator().showTaskDetails(viewModel.getList() as TaskList, task)
                    }
                }
            }

        })

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            adapter.tasks = it
        })

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.taskRecyclerView.layoutManager = layoutManager
        binding.taskRecyclerView.adapter = adapter

        return binding.root
    }
}