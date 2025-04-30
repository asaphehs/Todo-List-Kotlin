package com.example.todo_kotlin.service

import com.example.todo_kotlin.dto.TaskRequest
import com.example.todo_kotlin.dto.TaskResponse
import com.example.todo_kotlin.model.Task
import com.example.todo_kotlin.repository.TaskRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository
) {

    fun getAllTasks(): List<TaskResponse> {
        return taskRepository.findAll().map { toResponse(it) }
    }

    fun getTaskById(id: Long): TaskResponse {
        val task = taskRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Tarefa não encontrada com ID: $id") }
        return toResponse(task)
    }

    fun createTask(request: TaskRequest): TaskResponse {
        val task = toEntity(request)
        val saved = taskRepository.save(task)
        return toResponse(saved)
    }

    fun updateTask(id: Long, request: TaskRequest): TaskResponse {
        val task = taskRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Tarefa não encontrada com ID: $id") }

        task.title = request.title
        task.description = request.description
        task.done = request.done
        task.priority = request.priority

        val updated = taskRepository.save(task)
        return toResponse(updated)
    }

    fun deleteTask(id: Long) {
        if (!taskRepository.existsById(id)) {
            throw EntityNotFoundException("Tarefa não encontrada com ID: $id")
        }
        taskRepository.deleteById(id)
    }

    // Conversão para Entidade
    private fun toEntity(request: TaskRequest): Task {
        return Task(
            title = request.title,
            description = request.description,
            done = request.done,
            priority = request.priority
        )
    }

    // Conversão para DTO de Resposta
    private fun toResponse(task: Task): TaskResponse {
        return TaskResponse(
            id = task.id,
            title = task.title,
            description = task.description,
            done = task.done,
            priority = task.priority
        )
    }
}
