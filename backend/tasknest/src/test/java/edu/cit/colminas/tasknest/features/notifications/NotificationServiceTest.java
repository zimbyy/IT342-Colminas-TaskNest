package edu.cit.colminas.tasknest.features.notifications;

import edu.cit.colminas.tasknest.features.notifications.model.Notification;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    @Test
    void notification_fieldsSetCorrectly() {
        Notification n = new Notification();
        n.setUserId("user-123");
        n.setMessage("Task deadline approaching");
        n.setType("deadline");
        n.setCreatedAt(LocalDateTime.now());
        assertEquals("user-123", n.getUserId());
        assertEquals("Task deadline approaching", n.getMessage());
        assertFalse(n.isRead());
    }

    @Test
    void notification_defaultRead_isFalse() {
        Notification n = new Notification();
        assertFalse(n.isRead());
    }

    @Test
    void notification_canBeMarkedRead() {
        Notification n = new Notification();
        n.setRead(true);
        assertTrue(n.isRead());
    }
}
