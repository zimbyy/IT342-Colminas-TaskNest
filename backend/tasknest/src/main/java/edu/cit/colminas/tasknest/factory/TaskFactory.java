package edu.cit.colminas.tasknest.factory;

import java.time.LocalDateTime;

import edu.cit.colminas.tasknest.model.Task;

public class TaskFactory {
    public static Task createTask(String title, LocalDateTime deadline) {
        return new Task(title, deadline);
    }
}