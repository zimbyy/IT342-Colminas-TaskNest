package edu.cit.colminas.tasknest.model;

import java.time.LocalDateTime;

public class Task {
    private String title;
    private LocalDateTime deadline;

    public Task(String title, LocalDateTime deadline) {
        this.title = title;
        this.deadline = deadline;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }
}