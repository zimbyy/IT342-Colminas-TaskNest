package edu.cit.colminas.tasknest.strategy;

import edu.cit.colminas.tasknest.model.Task;
import java.util.List;

public interface TaskSortingStrategy {
    void sort(List<Task> tasks);
}