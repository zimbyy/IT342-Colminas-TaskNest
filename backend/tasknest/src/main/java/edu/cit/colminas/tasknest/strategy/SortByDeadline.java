package edu.cit.colminas.tasknest.strategy;

import edu.cit.colminas.tasknest.features.taskmanagement.model.Task;
import java.util.Comparator;
import java.util.List;

public class SortByDeadline implements TaskSortingStrategy {
    @Override
    public void sort(List<Task> tasks) {
        tasks.sort(Comparator.comparing(Task::getDeadline));
    }
}