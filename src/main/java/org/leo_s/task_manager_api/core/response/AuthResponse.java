package org.leo_s.task_manager_api.core.response;

import java.util.UUID;

public record AuthResponse(UUID userId, String token) {}