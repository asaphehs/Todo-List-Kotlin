package com.example.todo_kotlin.dto

import com.example.todo_kotlin.model.Priority

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val priority: Priority,
    val done: Boolean
)
