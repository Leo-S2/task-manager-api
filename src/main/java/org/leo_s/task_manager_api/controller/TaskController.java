package org.leo_s.task_manager_api.controller;

import jakarta.validation.Valid;
import org.leo_s.task_manager_api.core.request.ExtendTaskTimeRequest;
import org.leo_s.task_manager_api.core.request.TaskRequest;
import org.leo_s.task_manager_api.core.request.UpdateTaskTitleRequest;
import org.leo_s.task_manager_api.core.response.TaskResponse;
import org.leo_s.task_manager_api.core.task.TaskStatus;
import org.leo_s.task_manager_api.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskResponse> getTasks(@RequestParam(required = false) TaskStatus status,
                                       @RequestParam(required = false) String name) {
        return service.getTasks(status, name);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        TaskResponse savedTask = service.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id) {
        TaskResponse updatedTask = service.completeTask(id);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/title")
    public ResponseEntity<TaskResponse> updateTitle(@PathVariable Long id, @Valid @RequestBody UpdateTaskTitleRequest request) {
        return ResponseEntity.ok(service.updateTitleTask(id, request.title()));
    }

    @PutMapping("/{id}/extend-time")
    public ResponseEntity<TaskResponse> extendTime(@PathVariable Long id, @Valid @RequestBody ExtendTaskTimeRequest request) {
        return ResponseEntity.ok(service.extendTimeTask(id, request.durationSeconds()));
    }
}