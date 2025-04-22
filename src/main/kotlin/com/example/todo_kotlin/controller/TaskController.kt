package com.example.todo_kotlin.controller

import com.example.todo_kotlin.dto.TaskRequest
import com.example.todo_kotlin.dto.TaskResponse
import com.example.todo_kotlin.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService
) {
    @GetMapping
    fun getAll(): List<TaskResponse> = taskService.getAllTasks()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): TaskResponse = taskService.getTaskById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: TaskRequest): TaskResponse =
        taskService.createTask(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid request: TaskRequest): TaskResponse =
        taskService.updateTask(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = taskService.deleteTask(id)
}