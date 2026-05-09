package edu.cit.colminas.tasknest.features.taskmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.colminas.tasknest.shared.dto.ApiResponse;
import edu.cit.colminas.tasknest.features.taskmanagement.model.Task;
import edu.cit.colminas.tasknest.features.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Task>>> getTasks(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getTasksByUserIdWithStatus(userId);
            return ResponseEntity.ok(ApiResponse.success(tasks));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Task>> createTask(@PathVariable Long userId, @RequestBody Task task) {
        try {
            Task createdTask = taskService.createTask(userId, task);
            return ResponseEntity.ok(ApiResponse.success(createdTask));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Task>> updateTask(@PathVariable String taskId, @RequestBody Task task) {
        try {
            Task updatedTask = taskService.updateTaskWithStatusCheck(taskId, task);
            return ResponseEntity.ok(ApiResponse.success(updatedTask));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok(ApiResponse.success("Task deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}