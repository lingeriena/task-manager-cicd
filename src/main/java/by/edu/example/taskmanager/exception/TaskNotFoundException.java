package by.edu.example.taskmanager.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(long id) {
        super("Задача с идентификатором " + id + " не найдена");
    }
}