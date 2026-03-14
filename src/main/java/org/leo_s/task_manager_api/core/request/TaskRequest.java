package org.leo_s.task_manager_api.core.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank(message = "Title cannot be empty")
        @Size(min = 4, message = "The title cannot be less than 4 characters")
        String title,

        @Min(value = 1, message = "Duration must be greater than 0")
        long durationSeconds
) {}