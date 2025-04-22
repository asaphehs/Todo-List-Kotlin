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
){
    fun getAllTasks(): List<TaskResponse> =
        taskRepository.findAll().map { it.toResponse() }

    fun getTaskById(id: Long): TaskResponse {
        val task = taskRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Tarefa não encontrada com ID: $id") }
        return task.toResponse()
    }

    fun createTask(request: TaskRequest): TaskResponse {
        val task = Task(
            title = request.title,
            description = request.description,
            done = request.done
        )
        return taskRepository.save(task).toResponse()
    }

    fun updateTask(id: Long, request: TaskRequest): TaskResponse {
        val task = taskRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Tarefa não encontrada com ID: $id") }

        task.title = request.title
        task.description = request.description
        task.done = request.done

        return taskRepository.save(task).toResponse()
    }

    fun deleteTask(id: Long) {
        if (!taskRepository.existsById(id)) {
            throw EntityNotFoundException("Tarefa não encontrada com ID: $id")
        }
        taskRepository.deleteById(id)
    }

    // Função de extensão para converter Task → TaskResponse
    private fun Task.toResponse() = TaskResponse(
        id = this.id,
        title = this.title,
        description = this.description,
        done = this.done
    )
}
