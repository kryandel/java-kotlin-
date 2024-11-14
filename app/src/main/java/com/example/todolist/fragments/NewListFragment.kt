package com.example.todolist.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolist.R
import com.example.todolist.databinding.FragmentNewListBinding
import kotlin.math.log

class NewListFragment : Fragment() {

    private lateinit var binding: FragmentNewListBinding

    private val viewModel: NewListViewModel by viewModels{ factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewListBinding.inflate(layoutInflater, container, false)

        binding.buttonComplete.isEnabled = false

        binding.textField.doOnTextChanged { text, _, _, _ ->
            if (text == null || text.toString().isEmpty()) {
                binding.buttonComplete.isEnabled = false
                viewModel.name = String()
            } else {
                binding.buttonComplete.isEnabled = true
                viewModel.name = text.toString()
            }
        }

        binding.buttonComplete.setOnClickListener {
            viewModel.createList()
            navigator().goBack()
        }

        binding.buttonClose.setOnClickListener {
            navigator().goBack()
        }

        return binding.root
    }
}