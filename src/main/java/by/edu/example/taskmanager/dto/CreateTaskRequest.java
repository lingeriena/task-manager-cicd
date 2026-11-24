package by.edu.example.taskmanager.dto;

import by.edu.example.taskmanager.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "Название задачи не должно быть пустым")
        @Size(max = 120, message = "Название не должно превышать 120 символов")
        String title,
        @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
        String description,
        TaskPriority priority) {

    public CreateTaskRequest(String title, String description) {
        this(title, description, null);
    }
}