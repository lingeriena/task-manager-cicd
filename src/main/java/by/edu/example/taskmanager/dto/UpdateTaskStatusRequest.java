package by.edu.example.taskmanager.dto;

import by.edu.example.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
        @NotNull(message = "Статус обязателен")
        TaskStatus status) {
}