package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.todolist.ListActionListener
import com.example.todolist.ListAdapter
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.model.TaskList

class ListFragment : Fragment() {

    private lateinit var binding : FragmentListBinding
    private lateinit var adapter : ListAdapter

    private val viewModel: ListViewModel by viewModels{ factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        adapter = ListAdapter(object : ListActionListener {
            override fun onListMove(list: TaskList, moveBy: Int) {
                viewModel.moveList(list, moveBy)
            }

            override fun onListDelete(list: TaskList) {
                viewModel.deleteList(list)
            }

            override fun onListDetails(list: TaskList) {
                navigator().showListDetails(list)
            }

        })

        viewModel.lists.observe(viewLifecycleOwner, Observer {
            adapter.lists = it
        })

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.listRecyclerView.layoutManager = layoutManager
        binding.listRecyclerView.adapter = adapter

        return binding.root
    }
}