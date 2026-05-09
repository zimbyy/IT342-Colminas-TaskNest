package edu.cit.colminas.tasknest.features.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.cit.colminas.tasknest.features.taskmanagement.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByUserId(Long userId);
    List<Task> findByStatusNot(String status);
}