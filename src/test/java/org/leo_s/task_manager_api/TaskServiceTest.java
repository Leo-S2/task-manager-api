package org.leo_s.task_manager_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leo_s.task_manager_api.core.task.Task;
import org.leo_s.task_manager_api.core.user.User;
import org.leo_s.task_manager_api.repository.TaskRepository;
import org.leo_s.task_manager_api.service.CurrentUserService;
import org.leo_s.task_manager_api.service.TaskService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CurrentUserService currentUserService;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, currentUserService);
    }

    @Test
    void deleteTask_shouldDeleteOwnedTask() {
        UUID userId = UUID.randomUUID();
        User currentUser = mock(User.class);
        Task task = mock(Task.class);

        when(currentUser.getId()).thenReturn(userId);
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(taskRepository.findByIdAndUserId(1L, userId)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void getTaskById_shouldThrowWhenTaskDoesNotExist() {
        UUID userId = UUID.randomUUID();
        User currentUser = mock(User.class);

        when(currentUser.getId()).thenReturn(userId);
        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(taskRepository.findByIdAndUserId(99L, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Task not found");
    }
}