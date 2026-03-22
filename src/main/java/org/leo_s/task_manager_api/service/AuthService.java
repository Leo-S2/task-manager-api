package org.leo_s.task_manager_api.service;

import org.leo_s.task_manager_api.core.request.LoginRequest;
import org.leo_s.task_manager_api.core.request.UserRequest;
import org.leo_s.task_manager_api.core.response.AuthResponse;
import org.leo_s.task_manager_api.core.response.UserResponse;
import org.leo_s.task_manager_api.core.user.Role;
import org.leo_s.task_manager_api.core.user.User;
import org.leo_s.task_manager_api.repository.TaskRepository;
import org.leo_s.task_manager_api.repository.UserRepository;
import org.leo_s.task_manager_api.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CurrentUserService currentUserService;

    public AuthService(UserRepository userRepository, TaskRepository taskRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.currentUserService = currentUserService;
    }

    public UserResponse register(UserRequest request) {
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

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String token = jwtService.generateToken(request.email());
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new AuthResponse(user.getId(), token);
    }

    @Transactional
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(user.getRole() != Role.ADMIN){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not an administrator to perform this action!");
        }

        taskRepository.deleteByUser_Id(userId);
        userRepository.delete(user);
    }

    @Transactional
    public void deleteCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();
        taskRepository.deleteByUser_Id(currentUser.getId());
        userRepository.delete(currentUser);
    }
}