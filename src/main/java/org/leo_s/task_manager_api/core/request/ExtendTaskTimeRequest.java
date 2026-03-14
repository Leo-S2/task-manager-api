package org.leo_s.task_manager_api.core.request;

import jakarta.validation.constraints.Min;

public record ExtendTaskTimeRequest(
        @Min(value = 1, message = "Duration must be greater than 0")
        long durationSeconds
) {}