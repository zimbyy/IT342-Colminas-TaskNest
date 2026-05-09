package edu.cit.colminas.tasknest.features.notifications.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import edu.cit.colminas.tasknest.features.notifications.model.Notification;
import edu.cit.colminas.tasknest.features.taskmanagement.observer.Observer;
import edu.cit.colminas.tasknest.features.taskmanagement.observer.DeadlineNotifier;
import edu.cit.colminas.tasknest.features.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements Observer {
    
    private final NotificationRepository notificationRepository;
    private final DeadlineNotifier notifier;

    @PostConstruct
    public void init() {
        notifier.addObserver(this);
    }

    @Override
    public void update(String message) {
        // This method is called by the observer pattern
        // For now, we'll just log it
        System.out.println("Notification: " + message);
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    public Notification createNotification(String userId, String message, String type) {
        Notification notification = new Notification(userId, message, type);
        return notificationRepository.save(notification);
    }

    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAllAsRead(String userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public void notifyDeadlineApproaching(String userId, String taskTitle, LocalDateTime deadline) {
        String message = String.format("Deadline approaching for task: %s (Due: %s)", 
            taskTitle, deadline.toLocalDate());
        createNotification(userId, message, "DEADLINE_REMINDER");
    }

    public void notifyTaskOverdue(String userId, String taskTitle) {
        String message = String.format("Task overdue: %s", taskTitle);
        createNotification(userId, message, "TASK_OVERDUE");
    }
}