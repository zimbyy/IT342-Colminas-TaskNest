package edu.cit.colminas.tasknest.factory;

import java.time.LocalDateTime;

import edu.cit.colminas.tasknest.model.Task;
import edu.cit.colminas.tasknest.model.User;

public class TaskFactory {
    public static Task createTask(User user, String title, LocalDateTime deadline) {
        Task task = new Task();
        task.setUser(user);
        task.setTitle(title);
        task.setDeadline(deadline);
        task.setIsCompleted(false);
        return task;
    }
    
    public static Task createTask(User user, String title, String description, LocalDateTime deadline) {
        Task task = new Task();
        task.setUser(user);
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setIsCompleted(false);
        return task;
    }
}