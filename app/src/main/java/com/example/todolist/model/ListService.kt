package com.example.todolist.model

interface ListService {

    fun createList(task: TaskList)

    fun getList(id: Int) : Result<TaskList>

    fun moveList(list: TaskList, moveBy: Int)

    fun deleteList(list : TaskList)

    fun addListener(listener : ListListener)

    fun removeListener(listener : ListListener)
}