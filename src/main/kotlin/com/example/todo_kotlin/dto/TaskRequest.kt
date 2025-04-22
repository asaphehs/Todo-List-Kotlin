package com.example.todo_kotlin.dto

import jakarta.validation.constraints.NotBlank

data class TaskRequest(
    @field:NotBlank(message = "O título é obrigatório.")
    val title: String,

    val description: String? = null,

    val done: Boolean = false
)
