package com.todoapp.controller;

import com.todoapp.model.Task;
import com.todoapp.security.UserDetailsImpl;
import com.todoapp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TodoController {

    @Autowired
    private TodoService todoService;

    // GET all tasks for the authenticated user
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Task> tasks = todoService.getAllTasksByUserId(userDetails.getId());
        return ResponseEntity.ok(tasks);
    }

    // GET a single task by ID for the authenticated user
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        try {
            Task task = todoService.getTaskByIdAndUserId(id, userDetails.getId());
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Create a new task for the authenticated user
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Task createdTask = todoService.createTask(task, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // PUT - Update an existing task for the authenticated user
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        try {
            Task updatedTask = todoService.updateTask(id, taskDetails, userDetails.getId());
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE a task for the authenticated user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        try {
            todoService.deleteTask(id, userDetails.getId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}