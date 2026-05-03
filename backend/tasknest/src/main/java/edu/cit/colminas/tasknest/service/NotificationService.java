package edu.cit.colminas.tasknest.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import edu.cit.colminas.tasknest.model.Notification;
import edu.cit.colminas.tasknest.observer.Observer;
import edu.cit.colminas.tasknest.observer.DeadlineNotifier;
import edu.cit.colminas.tasknest.repository.NotificationRepository;
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

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    public Notification createNotification(Long userId, String message, String type) {
        Notification notification = new Notification(userId, message, type);
        return notificationRepository.save(notification);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public void notifyDeadlineApproaching(Long userId, String taskTitle, LocalDateTime deadline) {
        String message = String.format("Deadline approaching for task: %s (Due: %s)", 
            taskTitle, deadline.toLocalDate());
        createNotification(userId, message, "DEADLINE_REMINDER");
    }

    public void notifyTaskOverdue(Long userId, String taskTitle) {
        String message = String.format("Task overdue: %s", taskTitle);
        createNotification(userId, message, "TASK_OVERDUE");
    }
}