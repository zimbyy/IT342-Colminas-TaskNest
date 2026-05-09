package edu.cit.colminas.tasknest.features.taskmanagement.observer;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String message);
}