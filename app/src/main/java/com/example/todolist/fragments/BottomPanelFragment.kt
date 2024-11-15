package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolist.databinding.FragmentBottomPanelBinding
import com.example.todolist.model.TaskList

class BottomPanelFragment : Fragment() {

    private lateinit var binding: FragmentBottomPanelBinding

    private val viewModel: BottomPanelViewModel by viewModels{ factory() }

    private val listener: BottomPanelListener = {
        if (it == null) {
            deactivateButtonNew()
            deactivateButtonDetails()
        } else {
            when (it.listType) {
                TaskList.ListType.FAVOURITE -> {
                    deactivateButtonNew()
                    deactivateButtonDetails()
                }
                TaskList.ListType.NEW_BUTTON -> {
                    deactivateButtonNew()
                    deactivateButtonDetails()
                }
                TaskList.ListType.USER_LIST -> {
                    activateButtonNew()
                    activateButtonDetails()
                }
            }
        }
    }

    private fun activateButtonNew() {
        binding.buttonNew.isEnabled = true
        binding.buttonNew.isVisible = true
    }

    private fun deactivateButtonNew() {
        binding.buttonNew.isEnabled = false
        binding.buttonNew.isVisible = false
    }

    private fun activateButtonDetails() {
        binding.buttonDetails.isEnabled = true
        binding.buttonDetails.isVisible = true
    }

    private fun deactivateButtonDetails() {
        binding.buttonDetails.isEnabled = false
        binding.buttonDetails.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.addListener(listener)
        binding = FragmentBottomPanelBinding.inflate(layoutInflater, container, false)

        if (viewModel.list.value != null &&
            (viewModel.list.value as TaskList).listType == TaskList.ListType.USER_LIST) {
            activateButtonNew()
            activateButtonDetails()
        } else {
            deactivateButtonNew()
            deactivateButtonDetails()
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeListener(listener)
    }
}