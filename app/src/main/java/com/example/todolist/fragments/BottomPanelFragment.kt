package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolist.databinding.FragmentBottomPanelBinding
import com.example.todolist.model.TaskList

class BottomPanelFragment : Fragment() {

    private lateinit var binding: FragmentBottomPanelBinding

    private val viewModel: BottomPanelViewModel by viewModels{ factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomPanelBinding.inflate(layoutInflater, container, false)

        binding.buttonAllLists.setOnClickListener {
            //todo all lists vertical
        }

        binding.buttonDetails.setOnClickListener {
            if (viewModel.list.value != null) {
                navigator().showListDetails(viewModel.list.value as TaskList)
            }
        }

        binding.buttonNew.setOnClickListener {
            navigator().showNewTask()
        }

        return binding.root
    }
}