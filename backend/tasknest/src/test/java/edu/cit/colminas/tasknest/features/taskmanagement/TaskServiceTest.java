package edu.cit.colminas.tasknest.features.taskmanagement;

import edu.cit.colminas.tasknest.features.taskmanagement.model.Task;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Test
    void task_fieldsSetCorrectly() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("A description");
        task.setDeadline(LocalDateTime.of(2025, 12, 31, 23, 59));
        task.setStatus("pending");
        assertEquals("Test Task", task.getTitle());
        assertEquals("pending", task.getStatus());
        assertNotNull(task.getDeadline());
    }

    @Test
    void task_defaultStatus_isPending() {
        Task task = new Task();
        assertEquals("pending", task.getStatus());
    }

    @Test
    void task_statusCanBeUpdated() {
        Task task = new Task();
        task.setStatus("completed");
        assertEquals("completed", task.getStatus());
    }
}
