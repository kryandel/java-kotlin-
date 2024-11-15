package com.example.todolist

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemSubtaskBinding
import com.example.todolist.model.Task

class SubtaskAdapter(
    private val actionListener: SubtaskActionListener
) : RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder>(), View.OnClickListener {

    class SubtaskViewHolder (
        val binding: ItemSubtaskBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var subtasks: List<Task> = mutableListOf()
        set(new_value) {
            field = new_value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSubtaskBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return SubtaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubtaskViewHolder, position: Int) {
        val subtask = subtasks[position]
        with (holder.binding) {
            holder.itemView.tag = subtask
            subtaskName.setText(subtask.name)
        }

        holder.binding.buttonRemoveSubtask.setOnClickListener {
            actionListener.deleteSubtask(subtask)
        }

        holder.binding.subtaskName.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                actionListener.renameSubtask(subtask, text.toString())
            }
        }
    }

    override fun getItemCount(): Int = subtasks.size

    override fun onClick(v: View) {}


}