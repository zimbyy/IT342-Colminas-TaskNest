package edu.cit.colminas.tasknest.service;

import edu.cit.colminas.tasknest.model.Task;
import edu.cit.colminas.tasknest.repository.TaskRepository;
import edu.cit.colminas.tasknest.observer.DeadlineNotifier;
import java.time.LocalDateTime;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository = TaskRepository.getInstance();
    private final DeadlineNotifier deadlineNotifier = new DeadlineNotifier();

    public void addTask(Task task) {
        taskRepository.save(task);
        deadlineNotifier.notifyObservers("New task added: " + task.getTitle());
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void checkDeadlines() {
        LocalDateTime now = LocalDateTime.now();
        for (Task task : taskRepository.findAll()) {
            if (task.getDeadline().isBefore(now.plusDays(1))) {
                deadlineNotifier.notifyObservers("Deadline approaching for task: " + task.getTitle());
            }
        }
    }

    public DeadlineNotifier getDeadlineNotifier() {
        return deadlineNotifier;
    }
}