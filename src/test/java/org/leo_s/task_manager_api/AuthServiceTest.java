package org.leo_s.task_manager_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leo_s.task_manager_api.core.request.UserRequest;
import org.leo_s.task_manager_api.core.response.UserResponse;
import org.leo_s.task_manager_api.core.user.User;
import org.leo_s.task_manager_api.repository.TaskRepository;
import org.leo_s.task_manager_api.repository.UserRepository;
import org.leo_s.task_manager_api.security.JwtService;
import org.leo_s.task_manager_api.service.AuthService;
import org.leo_s.task_manager_api.service.CurrentUserService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CurrentUserService currentUserService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                taskRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                currentUserService
        );
    }

    @Test
    void register_shouldEncodePasswordAndSaveUser() {
        UserRequest request = new UserRequest("Leo", "leo@example.com", "12345678");

        when(userRepository.existsByEmail("leo@example.com")).thenReturn(false);
        when(passwordEncoder.encode("12345678")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getEmail()).isEqualTo("leo@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password");
        assertThat(response).isNotNull();
    }

    @Test
    void deleteCurrentUser_shouldDeleteTasksAndUser() {
        UUID userId = UUID.randomUUID();
        User currentUser = mock(User.class);

        when(currentUser.getId()).thenReturn(userId);
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);

        authService.deleteCurrentUser();

        verify(taskRepository).deleteByUser_Id(userId);
        verify(userRepository).delete(currentUser);
    }
}