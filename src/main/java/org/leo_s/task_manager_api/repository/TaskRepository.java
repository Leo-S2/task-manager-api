package org.leo_s.task_manager_api.repository;

import org.leo_s.task_manager_api.core.task.Task;
import org.leo_s.task_manager_api.core.task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByNameContainingIgnoreCase(String name);
    List<Task> findByStatusAndNameContainingIgnoreCase(TaskStatus status, String name);
}
