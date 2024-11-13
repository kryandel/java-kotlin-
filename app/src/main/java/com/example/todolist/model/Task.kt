package com.example.todolist.model

import java.sql.Date

data class Task (
    val id: Long,
    val name: String,
    val date: Date,
    val description : String,
    val isFavourite: Boolean,
    val subtasks: List<Long>
)
