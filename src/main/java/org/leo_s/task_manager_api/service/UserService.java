package org.leo_s.task_manager_api.service;

import org.leo_s.task_manager_api.core.request.UserRequest;
import org.leo_s.task_manager_api.core.response.UserResponse;
import org.leo_s.task_manager_api.core.user.Role;
import org.leo_s.task_manager_api.core.user.User;
import org.leo_s.task_manager_api.repository.TaskRepository;
import org.leo_s.task_manager_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TaskRepository taskRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = new User(
                request.name(),
                Role.USER,
                request.email(),
                passwordEncoder.encode(request.password())
        );

        return UserResponse.of(userRepository.save(user));
    }

    public void login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        taskRepository.deleteByUser_Id(userId);
        userRepository.delete(user);
    }
}