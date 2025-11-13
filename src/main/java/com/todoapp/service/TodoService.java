package com.todoapp.service;

import com.todoapp.model.Task;
import com.todoapp.model.User;
import com.todoapp.repository.TaskRepository;
import com.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all tasks for a specific user
    public List<Task> getAllTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    // Get a task by ID and user ID
    public Task getTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
    }

    // Create a new task for a user
    public Task createTask(Task task, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        task.setUser(user);
        return taskRepository.save(task);
    }

    // Update an existing task
    public Task updateTask(Long taskId, Task taskDetails, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());

        return taskRepository.save(task);
    }

    // Delete a task
    @Transactional
    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        taskRepository.delete(task);
    }
}