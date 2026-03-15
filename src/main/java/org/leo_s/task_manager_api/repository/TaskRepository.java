package org.leo_s.task_manager_api.repository;

import org.leo_s.task_manager_api.core.task.Task;
import org.leo_s.task_manager_api.core.task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(UUID userId);
    List<Task> findByUserIdAndStatus(UUID userId, TaskStatus status);
    List<Task> findByUserIdAndNameContainingIgnoreCase(UUID userId, String name);
    List<Task> findByUserIdAndStatusAndNameContainingIgnoreCase(UUID userId, TaskStatus status, String name);
    Optional<Task> findByIdAndUserId(Long id, UUID userId);
    void deleteByUser_Id(UUID userId);
}
