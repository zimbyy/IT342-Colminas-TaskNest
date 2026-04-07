package edu.cit.colminas.tasknest.repository;

import edu.cit.colminas.tasknest.model.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository instance;
    private final List<Task> tasks = new ArrayList<>();

    private TaskRepository() {}

    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    public void save(Task task) {
        tasks.add(task);
    }

    public List<Task> findAll() {
        return tasks;
    }
}