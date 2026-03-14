package org.leo_s.task_manager_api.service;

import org.leo_s.task_manager_api.core.request.TaskRequest;
import org.leo_s.task_manager_api.core.response.TaskResponse;
import org.leo_s.task_manager_api.core.task.Task;
import org.leo_s.task_manager_api.core.task.TaskStatus;
import org.leo_s.task_manager_api.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<TaskResponse> getTasks(TaskStatus status, String name) {
        List<Task> tasks;

        if(status == null && (name == null || name.isEmpty())){
            tasks = repository.findAll();
        }else if(status != null && (name == null || name.isEmpty())){
            tasks = repository.findByStatus(status);
        }else if(status == null){
            tasks = repository.findByNameContainingIgnoreCase(name);
        }else{
            tasks = repository.findByStatusAndNameContainingIgnoreCase(status, name);
        }

        return tasks.stream()
                .map(TaskResponse::of)
                .toList();
    }

    public TaskResponse getTaskById(Long id) {
        return TaskResponse.of(repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
    }

    private Task getTaskEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public TaskResponse createTask(TaskRequest request) {
        long createdAt = System.currentTimeMillis();
        long durationMillis = TimeUnit.SECONDS.toMillis(request.durationSeconds());

        return TaskResponse.of(repository.save(new Task(
                request.title(),
                createdAt,
                durationMillis)
        ));
    }

    public void deleteTask(Long id) {
        Task task = getTaskEntityById(id);
        repository.delete(task);
    }

    public TaskResponse completeTask(Long id) {
        Task task = getTaskEntityById(id);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is already completed");
        }

        task.setStatus(TaskStatus.COMPLETE);
        return TaskResponse.of(repository.save(task));
    }

    public TaskResponse updateTitleTask(Long id, String title) {
        Task task = getTaskEntityById(id);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed task cannot be updated");
        }

        task.setName(title);
        return TaskResponse.of(repository.save(task));
    }

    public TaskResponse extendTimeTask(Long id, long durationSeconds) {
        Task task = getTaskEntityById(id);

        if (task.getStatus() == TaskStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed task cannot be updated");
        }

        task.addEndAt(TimeUnit.SECONDS.toMillis(durationSeconds));
        return TaskResponse.of(repository.save(task));
    }
}