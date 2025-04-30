package com.example.todo_kotlin.dto

import com.example.todo_kotlin.model.Priority
import jakarta.validation.constraints.NotBlank

data class TaskRequest(
    @field:NotBlank(message = "O título é obrigatório.")
    var title: String,

    var description: String? = null,

    var priority: Priority = Priority.MEDIUM,

    var done: Boolean = false


)
