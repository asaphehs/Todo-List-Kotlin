package com.example.todo_kotlin.model

import com.example.todo_kotlin.model.Priority.MEDIUM
import jakarta.persistence.*


@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    var description: String? = null,

    @Column(nullable = false)
    var done: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var priority: Priority = MEDIUM
)