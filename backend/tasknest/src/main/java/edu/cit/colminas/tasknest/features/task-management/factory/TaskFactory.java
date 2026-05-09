package edu.cit.colminas.tasknest.features.taskmanagement.factory;

import java.time.LocalDateTime;

import edu.cit.colminas.tasknest.features.taskmanagement.model.Task;

public class TaskFactory {
    public static Task createTask(String title, LocalDateTime deadline) {
        return new Task(title, deadline);
    }
}