package edu.cit.colminas.tasknest.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.cit.colminas.tasknest.model.Task;
import edu.cit.colminas.tasknest.model.User;
import edu.cit.colminas.tasknest.observer.DeadlineNotifier;
import edu.cit.colminas.tasknest.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final DeadlineNotifier deadlineNotifier;

    public Task createTask(User user, String title, String description, LocalDateTime deadline) {
        Task task = new Task();
        task.setUser(user);
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setIsCompleted(false);
        
        Task savedTask = taskRepository.save(task);
        deadlineNotifier.notifyObservers("New task created: " + title);
        return savedTask;
    }

    public List<Task> getUserTasks(Long userId) {
        return taskRepository.findByUserIdOrderByDeadlineAsc(userId);
    }

    public List<Task> getPendingTasks(Long userId) {
        return taskRepository.findByUserIdAndIsCompletedOrderByDeadlineAsc(userId, false);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, String title, String description, LocalDateTime deadline) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        
        return taskRepository.save(task);
    }

    public Task markTaskComplete(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setIsCompleted(true);
        return taskRepository.save(task);
    }

    public Task markTaskIncomplete(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setIsCompleted(false);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.deleteById(id);
    }

    public void checkDeadlines(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findByUserIdAndIsCompletedOrderByDeadlineAsc(userId, false);
        
        for (Task task : tasks) {
            if (task.getDeadline().isBefore(now.plusDays(1)) && task.getDeadline().isAfter(now)) {
                deadlineNotifier.notifyObservers("Deadline approaching: " + task.getTitle());
            }
        }
    }

    public DeadlineNotifier getDeadlineNotifier() {
        return deadlineNotifier;
    }
}