package org.leo_s.task_manager_api.service;

import org.leo_s.task_manager_api.core.request.TaskRequest;
import org.leo_s.task_manager_api.core.response.TaskResponse;
import org.leo_s.task_manager_api.core.task.Task;
import org.leo_s.task_manager_api.core.task.TaskStatus;
import org.leo_s.task_manager_api.core.user.User;
import org.leo_s.task_manager_api.repository.TaskRepository;
import org.leo_s.task_manager_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponse> getTasks(UUID userId, TaskStatus status, String name) {
        List<Task> tasks;

        if(status == null && (name == null || name.isEmpty())){
            tasks = taskRepository.findByUserId(userId);
        }else if(status != null && (name == null || name.isEmpty())){
            tasks = taskRepository.findByUserIdAndStatus(userId, status);
        }else if(status == null){
            tasks = taskRepository.findByUserIdAndNameContainingIgnoreCase(userId, name);
        }else{
            tasks = taskRepository.findByUserIdAndStatusAndNameContainingIgnoreCase(userId, status, name);
        }

        return tasks.stream()
                .map(TaskResponse::of)
                .toList();
    }

    public TaskResponse getTaskById(Long id, UUID userId) {
        return TaskResponse.of(taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
    }

    private Task getTaskEntityById(Long id, UUID userId) {
        return taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public TaskResponse createTask(TaskRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        long createdAt = System.currentTimeMillis();
        long durationMillis = TimeUnit.SECONDS.toMillis(request.durationSeconds());

        Task task = new Task(
                user,
                request.title(),
                createdAt,
                durationMillis
        );

        return TaskResponse.of(taskRepository.save(task));
    }

    public void deleteTask(Long id, UUID userId) {
        Task task = getTaskEntityById(id, userId);
        taskRepository.delete(task);
    }

    public TaskResponse completeTask(Long id, UUID userId) {
        Task task = getTaskEntityById(id, userId);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is already completed");
        }

        task.setStatus(TaskStatus.COMPLETE);
        return TaskResponse.of(taskRepository.save(task));
    }

    public TaskResponse updateTitleTask(Long id, UUID userId, String title) {
        Task task = getTaskEntityById(id, userId);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed task cannot be updated");
        }

        task.setName(title);
        return TaskResponse.of(taskRepository.save(task));
    }

    public TaskResponse extendTimeTask(Long id, UUID userId, long durationSeconds) {
        Task task = getTaskEntityById(id, userId);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed task cannot be updated");
        }

        task.addEndAt(TimeUnit.SECONDS.toMillis(durationSeconds));
        return TaskResponse.of(taskRepository.save(task));
    }
}