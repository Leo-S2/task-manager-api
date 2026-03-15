package org.leo_s.task_manager_api.core.response;

import org.leo_s.task_manager_api.core.user.Role;
import org.leo_s.task_manager_api.core.user.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        Role role,
        String email
) {
    public static UserResponse of(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getRole(),
                user.getEmail()
        );
    }
}