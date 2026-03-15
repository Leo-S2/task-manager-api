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
import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getTasks(@PathVariable UUID userId,
                                       @RequestParam(required = false) TaskStatus status,
                                       @RequestParam(required = false) String name) {
        return taskService.getTasks(userId, status, name);
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable UUID userId, @PathVariable Long id) {
        return taskService.getTaskById(id, userId);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@PathVariable UUID userId,
                                                   @Valid @RequestBody TaskRequest request) {
        TaskResponse savedTask = taskService.createTask(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID userId, @PathVariable Long id) {
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable UUID userId, @PathVariable Long id) {
        TaskResponse updatedTask = taskService.completeTask(id, userId);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/title")
    public ResponseEntity<TaskResponse> updateTitle(@PathVariable UUID userId, @PathVariable Long id,
                                                    @Valid @RequestBody UpdateTaskTitleRequest request) {
        return ResponseEntity.ok(taskService.updateTitleTask(id, userId, request.title()));
    }

    @PutMapping("/{id}/extend-time")
    public ResponseEntity<TaskResponse> extendTime(@PathVariable UUID userId, @PathVariable Long id,
                                                   @Valid @RequestBody ExtendTaskTimeRequest request) {
        return ResponseEntity.ok(taskService.extendTimeTask(id, userId, request.durationSeconds()));
    }
}