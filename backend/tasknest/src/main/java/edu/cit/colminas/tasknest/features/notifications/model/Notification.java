package edu.cit.colminas.tasknest.features.notifications.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private String userId;
    private String message;
    private String type; // DEADLINE_REMINDER, TASK_OVERDUE, etc.
    private LocalDateTime createdAt;
    private boolean read = false;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
    }

    public Notification(String userId, String message, String type) {
        this.userId = userId;
        this.message = message;
        this.type = type;
    }
}
