package edu.cit.colminas.tasknest.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.cit.colminas.tasknest.model.Task;
import edu.cit.colminas.tasknest.model.User;
import edu.cit.colminas.tasknest.repository.TaskRepository;
import edu.cit.colminas.tasknest.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Long userId, Task task) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task updated) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(updated.getTitle());
        task.setDescription(updated.getDescription());
        task.setDeadline(updated.getDeadline());
        task.setStatus(updated.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
    
    private void updateTaskStatus(Task task) {
        if (task.getDeadline() != null && !task.getStatus().equals("completed")) {
            if (task.getDeadline().isBefore(LocalDateTime.now())) {
                task.setStatus("overdue");
            } else if (task.getStatus().equals("overdue")) {
                task.setStatus("pending");
            }
        }
    }
    
    public List<Task> getTasksByUserIdWithStatus(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        tasks.forEach(this::updateTaskStatus);
        return tasks;
    }
    
    public Task updateTaskWithStatusCheck(Long taskId, Task updated) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setTitle(updated.getTitle());
        task.setDescription(updated.getDescription());
        task.setDeadline(updated.getDeadline());
        task.setStatus(updated.getStatus());
        
        updateTaskStatus(task);
        return taskRepository.save(task);
    }
}