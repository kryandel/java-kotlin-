package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.ListActionListener
import com.example.todolist.ListAdapter
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.model.TaskList

class ListFragment : Fragment() {

    private lateinit var binding : FragmentListBinding
    private lateinit var adapter : ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        adapter = ListAdapter(object : ListActionListener {
            override fun onListMove(list: TaskList, moveBy: Int) {
                TODO("Not yet implemented")
            }

            override fun onListDelete(list: TaskList) {
                TODO("Not yet implemented")
            }

            override fun onListDetails(list: TaskList) {
                TODO("Not yet implemented")
            }

        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.listRecyclerView.layoutManager = layoutManager
        binding.listRecyclerView.adapter = adapter

        return binding.root
    }
}