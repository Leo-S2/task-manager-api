package org.leo_s.task_manager_api.core.task;

import jakarta.persistence.*;
import org.leo_s.task_manager_api.core.user.User;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private long createdAt;
    private long endAt;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    protected Task() {
    }

    public Task(User user, String name, long createdAt, long durationMillis) {
        this.user = user;
        this.name = name;
        this.createdAt = createdAt;
        this.endAt = createdAt + durationMillis;
        this.status = TaskStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getEndAt() {
        return endAt;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void addEndAt(long extraMillis) {
        this.endAt += extraMillis;
    }
}