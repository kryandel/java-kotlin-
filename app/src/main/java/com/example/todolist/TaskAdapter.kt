package com.example.todolist

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
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
            field = unwrapTask(new_value)
            notifyDataSetChanged()
        }

    fun unwrapTask(list: List<Task>) : List<Task> {
        val newList = mutableListOf<Task>()
        list.forEach { it1 ->
            newList.add(it1)
            it1.subtasks.forEach { it2 ->
                newList.add(it2)
            }
        }

        return newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return TaskViewHolder(binding)
    }

    private fun dpToPx(dp: Double): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun pxToDp(px: Int): Double {
        return px.toDouble() * Resources.getSystem().displayMetrics.density
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        with (holder.binding) {
            holder.itemView.tag = task
            taskName.text = task.name
            if (task.isCompleted) {
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

            val params = holder.binding.unwrappedItemTask.layoutParams as MarginLayoutParams

            if (task.isSubtask) {
                params.leftMargin += dpToPx(50.0)
            }

            holder.binding.unwrappedItemTask.layoutParams = params
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