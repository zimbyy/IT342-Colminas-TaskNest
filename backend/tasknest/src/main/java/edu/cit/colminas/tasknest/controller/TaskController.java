package edu.cit.colminas.tasknest.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.colminas.tasknest.model.Task;
import edu.cit.colminas.tasknest.model.User;
import edu.cit.colminas.tasknest.repository.UserRepository;
import edu.cit.colminas.tasknest.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskController {
    
    private final TaskService taskService;
    private final UserRepository userRepository;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserTasks(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getUserTasks(userId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<?> getPendingTasks(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getPendingTasks(userId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        try {
            var task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createTask(@PathVariable Long userId, @Valid @RequestBody Map<String, Object> request) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            String title = (String) request.get("title");
            String description = (String) request.getOrDefault("description", "");
            LocalDateTime deadline = LocalDateTime.parse((String) request.get("deadline"));
            
            if (title == null || title.trim().isEmpty()) {
                throw new RuntimeException("Task title is required");
            }
            
            Task task = taskService.createTask(user, title, description, deadline);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String description = (String) request.getOrDefault("description", "");
            LocalDateTime deadline = LocalDateTime.parse((String) request.get("deadline"));
            
            if (title == null || title.trim().isEmpty()) {
                throw new RuntimeException("Task title is required");
            }
            
            Task task = taskService.updateTask(id, title, description, deadline);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeTask(@PathVariable Long id) {
        try {
            Task task = taskService.markTaskComplete(id);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PutMapping("/{id}/incomplete")
    public ResponseEntity<?> incompleteTask(@PathVariable Long id) {
        try {
            Task task = taskService.markTaskIncomplete(id);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Task deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PostMapping("/user/{userId}/check-deadlines")
    public ResponseEntity<?> checkDeadlines(@PathVariable Long userId) {
        try {
            taskService.checkDeadlines(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Deadline check completed");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}