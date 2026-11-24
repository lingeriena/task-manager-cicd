package by.edu.example.taskmanager.web;

import by.edu.example.taskmanager.dto.CreateTaskRequest;
import by.edu.example.taskmanager.dto.UpdateTaskPriorityRequest;
import by.edu.example.taskmanager.dto.UpdateTaskStatusRequest;
import by.edu.example.taskmanager.model.Task;
import by.edu.example.taskmanager.model.TaskPriority;
import by.edu.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> findAll(@RequestParam(required = false) TaskPriority priority) {
        return priority == null ? taskService.findAll() : taskService.findByPriority(priority);
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable long id) {
        return taskService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody CreateTaskRequest request) {
        Task created = taskService.create(request.title(), request.description(), request.priority());
        return ResponseEntity.created(URI.create("/api/tasks/" + created.id())).body(created);
    }

    @PatchMapping("/{id}/status")
    public Task changeStatus(@PathVariable long id, @Valid @RequestBody UpdateTaskStatusRequest request) {
        return taskService.changeStatus(id, request.status());
    }

    @PatchMapping("/{id}/priority")
    public Task changePriority(@PathVariable long id, @Valid @RequestBody UpdateTaskPriorityRequest request) {
        return taskService.changePriority(id, request.priority());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}