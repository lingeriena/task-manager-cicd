package by.edu.example.taskmanager.service;

import by.edu.example.taskmanager.exception.TaskNotFoundException;
import by.edu.example.taskmanager.model.Task;
import by.edu.example.taskmanager.model.TaskPriority;
import by.edu.example.taskmanager.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(0);

    public Task create(String title, String description) {
        return create(title, description, TaskPriority.NORMAL);
    }

    public Task create(String title, String description, TaskPriority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название задачи не должно быть пустым");
        }
        String normalizedTitle = title.trim();
        String normalizedDescription = description == null ? "" : description.trim();
        TaskPriority effectivePriority = priority == null ? TaskPriority.NORMAL : priority;
        long id = nextId.incrementAndGet();
        Task task = new Task(id, normalizedTitle, normalizedDescription, TaskStatus.NEW, effectivePriority, Instant.now());
        tasks.put(id, task);
        return task;
    }

    public List<Task> findAll() {
        return tasks.values().stream()
                .sorted(Comparator.comparingLong(Task::id))
                .toList();
    }

    public List<Task> findByPriority(TaskPriority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Приоритет для фильтрации обязателен");
        }
        return tasks.values().stream()
                .filter(task -> task.priority() == priority)
                .sorted(Comparator.comparingLong(Task::id))
                .toList();
    }

    public Task findById(long id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    public Task changeStatus(long id, TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Статус задачи обязателен");
        }
        return tasks.compute(id, (key, current) -> {
            if (current == null) {
                throw new TaskNotFoundException(id);
            }
            return new Task(current.id(), current.title(), current.description(), newStatus, current.priority(), current.createdAt());
        });
    }

    public Task changePriority(long id, TaskPriority newPriority) {
        if (newPriority == null) {
            throw new IllegalArgumentException("Приоритет задачи обязателен");
        }
        return tasks.compute(id, (key, current) -> {
            if (current == null) {
                throw new TaskNotFoundException(id);
            }
            return new Task(current.id(), current.title(), current.description(), current.status(), newPriority, current.createdAt());
        });
    }

    public void delete(long id) {
        Task removed = tasks.remove(id);
        if (removed == null) {
            throw new TaskNotFoundException(id);
        }
    }
}