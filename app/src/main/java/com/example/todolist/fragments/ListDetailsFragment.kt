package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.todolist.ListAdapter
import com.example.todolist.R
import com.example.todolist.databinding.FragmentListDetailsBinding
import com.example.todolist.model.TaskList

class ListDetailsFragment : Fragment() {

    private lateinit var binding : FragmentListDetailsBinding
    private lateinit var adapter: ListAdapter

    private val viewModel: ListDetailsViewModel by viewModels{ factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadList(requireArguments().getInt(ID_KEY))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListDetailsBinding.inflate(layoutInflater, container, false)

        viewModel.listDetails.observe(viewLifecycleOwner, Observer {
            binding.deleteListButton.isEnabled = it.id > 1
            binding.deleteComplitedTasksButton.isEnabled = it.tasks.any { task -> task.isComplited }
            binding.sortButton.isEnabled = it.sortType == TaskList.SortType.DEFAULT
            binding.sortType.text = when (it.sortType) {
                TaskList.SortType.DATE -> {
                    resources.getString(R.string.SortType_Date)
                }
                TaskList.SortType.FAVOURITE -> {
                    resources.getString(R.string.SortType_Favourite)
                }
                else -> {
                    resources.getString(R.string.SortType_Default)
                }
            }
        })

        binding.sortButton.setOnClickListener {
            //todo
        }

        binding.renameButton.setOnClickListener {}

        binding.deleteListButton.setOnClickListener {
            viewModel.deleteList()
            navigator().goBack()
        }

        binding.deleteComplitedTasksButton.setOnClickListener {
            viewModel.deleteCompletedTasks()
            navigator().goBack()
        }

        return binding.root
    }

    companion object {

        private const val ID_KEY = "ID_KEY"

        fun new(id: Int) = ListDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(ID_KEY, id)
            }
        }
    }
}