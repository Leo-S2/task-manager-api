package org.leo_s.task_manager_api.core.response;

import org.leo_s.task_manager_api.core.task.Task;
import org.leo_s.task_manager_api.core.task.TaskStatus;

public record TaskResponse(
        Long id,
        String name,
        long createdAt,
        long endAt,
        TaskStatus status
) {
    public static TaskResponse of(Task task){
        return new TaskResponse(
                task.getId(),
                task.getName(),
                task.getCreatedAt(),
                task.getEndAt(),
                task.getStatus());
    }
}
