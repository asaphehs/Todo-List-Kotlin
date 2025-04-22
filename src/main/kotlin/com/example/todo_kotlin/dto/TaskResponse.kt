package com.example.todo_kotlin.dto

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val done: Boolean
)
