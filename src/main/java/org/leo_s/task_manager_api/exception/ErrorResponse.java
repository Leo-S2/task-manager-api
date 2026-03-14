package org.leo_s.task_manager_api.exception;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path
) {
}