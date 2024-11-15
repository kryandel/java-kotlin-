package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.SubtaskActionListener
import com.example.todolist.SubtaskAdapter
import com.example.todolist.databinding.FragmentTaskDetailsBinding
import com.example.todolist.model.Task

class TaskDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailsBinding
    private lateinit var adapter: SubtaskAdapter

    private val viewModel: TaskDetailsViewModel by viewModels{ factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (requireArguments().getInt(ID_SUBTASK_KEY) != EMPTY_SUBTASK) {
            viewModel.loadSubtasks(
                requireArguments().getInt(ID_LIST_KEY), requireArguments().getInt(ID_TASK_KEY)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDetailsBinding.inflate(layoutInflater, container, false)
        adapter = SubtaskAdapter(object: SubtaskActionListener {
            override fun renameSubtask(subtask: Task, newName: String) {
                viewModel.renameSubtask(subtask, newName)
            }

            override fun deleteSubtask(subtask: Task) {
                viewModel.deleteSubtask(subtask)
            }
        })

        if (viewModel.subtask.value == null
            || (viewModel.subtask.value != null && (viewModel.subtask.value as Task).isSubtask)) {
            binding.sectionSubtasks.isEnabled = false
            binding.sectionSubtasks.isVisible = false

            binding.sectionAddSubtask.isEnabled = false
            binding.sectionAddSubtask.isVisible = false
        } else {
            binding.sectionSubtasks.isEnabled = true
            binding.sectionSubtasks.isVisible = true

            binding.sectionAddSubtask.isEnabled = true
            binding.sectionAddSubtask.isVisible = true
        }

        binding.buttonAddSubtask.isEnabled = false

        binding.editNewSubtaskName.doOnTextChanged { text, _, _, _ ->
            binding.buttonAddSubtask.isEnabled = !text.isNullOrEmpty()
        }

        binding.buttonAddSubtask.setOnClickListener {
            viewModel.addSubtask(binding.editNewSubtaskName.text.toString())
        }

        viewModel.subtask.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.subtasks = it.subtasks
            }
        })

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.itemSubtaskRecyclerView.layoutManager = layoutManager
        binding.itemSubtaskRecyclerView.adapter = adapter

        return binding.root
    }

    companion object {

        private const val ID_LIST_KEY = "ID_LIST_KEY"
        private const val ID_TASK_KEY = "ID_TASK_KEY"
        private const val ID_SUBTASK_KEY = "ID_SUBTASK_KEY"
        private const val EMPTY_SUBTASK = -1

        //todo
        //два аргумента подходят для Task, для SubTask нужно три

        fun new(idList: Int, idTask: Int, idSubtask: Int?) = TaskDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(ID_LIST_KEY, idList)
                putInt(ID_TASK_KEY, idTask)
                if (idSubtask != null) {
                    putInt(ID_SUBTASK_KEY, idSubtask)
                } else {
                    putInt(ID_SUBTASK_KEY, EMPTY_SUBTASK)
                }
            }
        }
    }
}