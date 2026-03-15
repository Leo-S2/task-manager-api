package org.leo_s.task_manager_api.core.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Name cannot be empty")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password
) {}