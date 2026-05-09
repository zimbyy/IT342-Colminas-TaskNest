package edu.cit.colminas.tasknest.features.taskmanagement.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    private String id;

    private String title;
    private String description;
    private LocalDateTime deadline;
    private String status = "pending";

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private edu.cit.colminas.tasknest.features.authentication.model.User user;

    public Task() {}

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    public Task(String title, LocalDateTime deadline) {
        this.title = title;
        this.deadline = deadline;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public edu.cit.colminas.tasknest.features.authentication.model.User getUser() { return user; }
    public void setUser(edu.cit.colminas.tasknest.features.authentication.model.User user) { this.user = user; }
}