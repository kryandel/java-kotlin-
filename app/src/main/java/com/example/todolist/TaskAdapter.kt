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
    }

    override fun getItemCount(): Int = tasks.size

    override fun onClick(v: View) {
        val task = v.tag as Task

        when (v.id) {
            R.id.taskName -> { actionListener.showDetails(task) }
            R.id.itemTaskCompletePickerON -> {
                actionListener.changeCompleteStatus(task, false)
                //todo status to subtasks
            }
            R.id.itemTaskCompletePickerOFF -> {
                actionListener.changeCompleteStatus(task, true)
                //todo status to subtasks
            }
            R.id.itemTaskFavouritePickerON -> {
                actionListener.changeFavouriteStatus(task, false)
            }
            R.id.itemTaskFavouritePickerOFF -> {
                actionListener.changeFavouriteStatus(task, true)
            }
            else -> {}
        }
    }


}