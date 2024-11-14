package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemListBinding
import com.example.todolist.model.TaskList

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.ListViewHolder>() {

    var lists : List<TaskList> = emptyList()
        set(new_value) {
            field = new_value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = lists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = lists[position]
        with(holder.binding) {
            listName.text = list.name
        }
    }

    class ListViewHolder (
        val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root)
}