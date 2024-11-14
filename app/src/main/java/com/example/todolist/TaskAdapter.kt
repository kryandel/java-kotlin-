package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTaskBinding
import com.example.todolist.model.Task

class TaskAdapter(
    private val actionListener: TaskActionListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), OnClickListener {

    class TaskViewHolder (
        val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var tasks: List<Task> = emptyList()
        set(new_value) {
            field = new_value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        with (holder.binding) {
            holder.itemView.tag = task
            taskName.text = task.name
            if (task.isComplited) {
                itemTaskCompletePickerOFF.isVisible = false
                itemTaskCompletePickerOFF.isEnabled = false

                itemTaskCompletePickerON.isVisible = true
                itemTaskCompletePickerON.isEnabled = true
            } else {
                itemTaskCompletePickerOFF.isVisible = true
                itemTaskCompletePickerOFF.isEnabled = true

                itemTaskCompletePickerON.isVisible = false
                itemTaskCompletePickerON.isEnabled = false
            }

            if (task.isFavourite) {
                itemTaskFavouritePickerOFF.isVisible = false
                itemTaskFavouritePickerOFF.isEnabled = false

                itemTaskFavouritePickerON.isVisible = true
                itemTaskFavouritePickerON.isEnabled = true
            } else {
                itemTaskFavouritePickerOFF.isVisible = true
                itemTaskFavouritePickerOFF.isEnabled = true

                itemTaskFavouritePickerON.isVisible = false
                itemTaskFavouritePickerON.isEnabled = false
            }
        }

        holder.binding.taskName.setOnClickListener {
            actionListener.showDetails(task)
        }
        holder.binding.itemTaskCompletePickerOFF.setOnClickListener {
            actionListener.changeCompleteStatus(task, true)
        }
        holder.binding.itemTaskCompletePickerON.setOnClickListener {
            actionListener.changeCompleteStatus(task, false)
        }
        holder.binding.itemTaskFavouritePickerOFF.setOnClickListener {
            actionListener.changeFavouriteStatus(task, true)
        }
        holder.binding.itemTaskFavouritePickerON.setOnClickListener {
            actionListener.changeFavouriteStatus(task, false)
        }

    }

    override fun getItemCount(): Int = tasks.size

    override fun onClick(v: View) {}


}