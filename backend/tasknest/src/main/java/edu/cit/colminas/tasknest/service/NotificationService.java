package edu.cit.colminas.tasknest.service;

import edu.cit.colminas.tasknest.observer.Observer;
import edu.cit.colminas.tasknest.observer.DeadlineNotifier;

public class NotificationService implements Observer {
    private final DeadlineNotifier notifier;

    public NotificationService(DeadlineNotifier notifier) {
        this.notifier = notifier;
        this.notifier.addObserver(this);
    }

    @Override
    public void update(String message) {
        System.out.println("Notification: " + message);
    }

    public void notifyDeadlineApproaching(String taskTitle) {
        System.out.println("Reminder: Deadline approaching for task: " + taskTitle);
    }
}