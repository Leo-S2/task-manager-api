package org.leo_s.task_manager_api.service;

import org.leo_s.task_manager_api.core.request.TaskRequest;
import org.leo_s.task_manager_api.core.response.TaskResponse;
import org.leo_s.task_manager_api.core.task.Task;
import org.leo_s.task_manager_api.core.task.TaskStatus;
import org.leo_s.task_manager_api.core.user.User;
import org.leo_s.task_manager_api.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CurrentUserService currentUserService;

    public TaskService(TaskRepository taskRepository, CurrentUserService currentUserService) {
        this.taskRepository = taskRepository;
        this.currentUserService = currentUserService;
    }

    public List<TaskResponse> getTasks(TaskStatus status, String name) {
        User user = currentUserService.getCurrentUser();
        List<Task> tasks;

        if(status == null && (name == null || name.isEmpty())){
            tasks = taskRepository.findByUserId(user.getId());
        }else if(status != null && (name == null || name.isEmpty())){
            tasks = taskRepository.findByUserIdAndStatus(user.getId(), status);
        }else if(status == null){
            tasks = taskRepository.findByUserIdAndNameContainingIgnoreCase(user.getId(), name);
        }else{
            tasks = taskRepository.findByUserIdAndStatusAndNameContainingIgnoreCase(user.getId(), status, name);
        }

        return tasks.stream()
                .map(TaskResponse::of)
                .toList();
    }

    public TaskResponse getTaskById(Long id) {
        return TaskResponse.of(getTaskEntityById(id));
    }

    private Task getTaskEntityById(Long id) {
        return taskRepository.findByIdAndUserId(id, currentUserService.getCurrentUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public TaskResponse createTask(TaskRequest request) {
        User user = currentUserService.getCurrentUser();

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

    public void deleteTask(Long id) {
        Task task = getTaskEntityById(id);
        taskRepository.delete(task);
    }

    public TaskResponse completeTask(Long id) {
        Task task = getTaskEntityById(id);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is already completed");
        }

        task.setStatus(TaskStatus.COMPLETE);
        return TaskResponse.of(taskRepository.save(task));
    }

    public TaskResponse updateTitleTask(Long id, String title) {
        Task task = getTaskEntityById(id);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed task cannot be updated");
        }

        task.setName(title);
        return TaskResponse.of(taskRepository.save(task));
    }

    public TaskResponse extendTimeTask(Long id, long durationSeconds) {
        Task task = getTaskEntityById(id);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed task cannot be updated");
        }

        task.addEndAt(TimeUnit.SECONDS.toMillis(durationSeconds));
        return TaskResponse.of(taskRepository.save(task));
    }
}