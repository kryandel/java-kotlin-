package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemListBinding
import com.example.todolist.model.TaskList

class ListAdapter(
    private val actionListener: ListActionListener
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>(), OnClickListener {

    var lists : List<TaskList> = emptyList()
        set(new_value) {
            field = new_value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = lists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = lists[position]
        with(holder.binding) {
            holder.itemView.tag = list
            listName.text = list.name
            if (position == lists.size - 1) {
                imageAddList.visibility = VISIBLE
            } else {
                imageAddList.visibility = INVISIBLE
            }
        }
    }

    class ListViewHolder (
        val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val list = v.tag as TaskList
        when (v.id) {
            R.id.list -> {
                when(list.listType) {
                    TaskList.ListType.NEW_BUTTON -> {
                        actionListener.createNewList()
                    }
                    else -> {
                        actionListener.selectList(list)
                    }
                }
            }
            else -> {}
        }
    }

}