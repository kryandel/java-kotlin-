package com.example.todolist

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.PopupMenu
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
        }
    }

    class ListViewHolder (
        val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val list = v.tag as TaskList
        when (v.id) {
            R.id.list -> {
                //showPopupMenu(v)
                actionListener.onListDetails(list)
            }
            else -> {
                //pass
            }
        }
    }

    fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val list = view.tag as TaskList
        val index = lists.indexOfFirst { it.id == list.id }

        popupMenu.menu.add(0, DELETE, Menu.NONE, context.getString(R.string.PopupMenuAction_Delete))
        popupMenu.menu.add(0, MOVE_LEFT, Menu.NONE, context.getString(R.string.PopupMenuAction_MoveLeft)).apply {
            isEnabled = index > 1
        }
        popupMenu.menu.add(0, MOVE_RIGHT, Menu.NONE, context.getString(R.string.PopupMenuAction_MoveRight)).apply {
            isEnabled = index < lists.size - 2
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                DELETE -> {
                    actionListener.onListDelete(list)
                }
                MOVE_LEFT -> {
                    actionListener.onListMove(list, -1)
                }
                MOVE_RIGHT -> {
                    actionListener.onListMove(list, 1)
                }
                else -> {}
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    companion object {
        private const val MOVE_LEFT = 1
        private const val MOVE_RIGHT = 2
        private const val DELETE = 3
    }

}