package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolist.databinding.FragmentNewTaskBinding
import java.sql.Date
import java.time.LocalDate

class NewTaskFragment : Fragment() {

    private lateinit var binding: FragmentNewTaskBinding

    private val viewModel: NewTaskViewModel by viewModels{ factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(layoutInflater, container, false)

        binding.datePicker.isVisible = false
        binding.datePicker.isEnabled = false

        binding.buttonPickFavouriteON.isVisible = false
        binding.buttonPickFavouriteON.isEnabled = false

        binding.buttonPickFavouriteOFF.isVisible = true
        binding.buttonPickFavouriteOFF.isEnabled = true

        binding.buttonSafeTask.isEnabled = false

        binding.buttonPickDate.setOnClickListener {
            binding.datePicker.isVisible = !binding.datePicker.isVisible
            binding.datePicker.isEnabled = !binding.datePicker.isEnabled
        }

        binding.datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            val date = LocalDate.of(year, monthOfYear, dayOfMonth).toEpochDay()
            viewModel.date = date
        }

        binding.buttonPickFavouriteOFF.setOnClickListener {
            binding.buttonPickFavouriteON.isVisible = true
            binding.buttonPickFavouriteON.isEnabled = true

            binding.buttonPickFavouriteOFF.isVisible = false
            binding.buttonPickFavouriteOFF.isEnabled = false

            viewModel.favourite = true
        }

        binding.buttonPickFavouriteON.setOnClickListener {
            binding.buttonPickFavouriteOFF.isVisible = true
            binding.buttonPickFavouriteOFF.isEnabled = true

            binding.buttonPickFavouriteON.isVisible = false
            binding.buttonPickFavouriteON.isEnabled = false

            viewModel.favourite = false
        }

        binding.textFieldNewTaskName.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.buttonSafeTask.isEnabled = false
                viewModel.name = String()
            } else {
                binding.buttonSafeTask.isEnabled = true
                viewModel.name = text.toString()
            }
        }

        binding.textFieldNewTaskDescription.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                viewModel.description = String()
            } else {
                viewModel.description = text.toString()
            }
        }

        binding.buttonSafeTask.setOnClickListener {
            viewModel.createTask()
            navigator().goBack()
        }

        return binding.root
    }
}