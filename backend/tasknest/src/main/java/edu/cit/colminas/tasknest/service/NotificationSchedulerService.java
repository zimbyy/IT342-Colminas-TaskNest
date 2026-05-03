package edu.cit.colminas.tasknest.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.cit.colminas.tasknest.model.Task;
import edu.cit.colminas.tasknest.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSchedulerService {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    // Run every minute to check for deadline reminders
    @Scheduled(cron = "0 * * * * ?")
    public void checkDeadlineReminders() {
        log.info("Running deadline reminder check...");
        
        // Get all tasks that are not completed
        List<Task> pendingTasks = taskRepository.findByStatusNot("completed");
        
        for (Task task : pendingTasks) {
            if (task.getDeadline() == null) continue;
            
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime deadline = task.getDeadline();
            
            // Check if deadline is within the next hour (3600 seconds)
            if (deadline.isAfter(now) && deadline.isBefore(now.plusHours(1))) {
                // Check if we haven't already sent a reminder for this task
                if (!hasRecentReminder(task, "DEADLINE_REMINDER")) {
                    notificationService.notifyDeadlineApproaching(
                        String.valueOf(task.getUser().getId()), 
                        task.getTitle(), 
                        deadline
                    );
                    log.info("Sent deadline reminder for task: {}", task.getTitle());
                }
            }
            
            // Check if task is overdue
            if (deadline.isBefore(now) && !"overdue".equals(task.getStatus())) {
                // Update task status to overdue
                task.setStatus("overdue");
                taskRepository.save(task);
                
                // Send overdue notification
                notificationService.notifyTaskOverdue(String.valueOf(task.getUser().getId()), task.getTitle());
                log.info("Marked task as overdue and sent notification: {}", task.getTitle());
            }
        }
    }
    
    private boolean hasRecentReminder(Task task, String notificationType) {
        // This is a simplified check - in production, you'd want to track 
        // which notifications have been sent for each task
        // For now, we'll just check if there are any recent notifications
        // of this type for this task's user
        try {
            List<edu.cit.colminas.tasknest.model.Notification> recentNotifications = 
                notificationService.getUnreadNotifications(String.valueOf(task.getUser().getId()));
            
            return recentNotifications.stream()
                .anyMatch(n -> n.getType().equals(notificationType) && 
                            n.getMessage().contains(task.getTitle()));
        } catch (Exception e) {
            log.warn("Error checking recent reminders for task {}: {}", task.getId(), e.getMessage());
            return false;
        }
    }
}
