package edu.cit.colminas.tasknest.controller;

import java.time.LocalDateTime;
import java.util.List;

import edu.cit.colminas.tasknest.factory.TaskFactory;
import edu.cit.colminas.tasknest.model.Task;
import edu.cit.colminas.tasknest.service.NotificationService;
import edu.cit.colminas.tasknest.service.TaskService;
import edu.cit.colminas.tasknest.strategy.TaskSortingStrategy;

public class TaskController {
    private final TaskService taskService = new TaskService();
    private final NotificationService notificationService = new NotificationService(taskService.getDeadlineNotifier());

    public void createTask(String title, LocalDateTime deadline) {
        Task task = TaskFactory.createTask(title, deadline);
        taskService.addTask(task);
    }

    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }

    public void checkDeadlines() {
        taskService.checkDeadlines();
    }

    public List<Task> getSortedTasks(TaskSortingStrategy strategy) {
        List<Task> tasks = taskService.getAllTasks();
        strategy.sort(tasks);
        return tasks;
    }
}