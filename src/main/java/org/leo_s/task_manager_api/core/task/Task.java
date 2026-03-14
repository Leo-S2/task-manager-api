package org.leo_s.task_manager_api.core.task;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private long createdAt;
    private long endAt;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    protected Task() {
    }

    public Task(String name, long createdAt, long durationMillis) {
        this.name = name;
        this.createdAt = createdAt;
        this.endAt = createdAt + durationMillis;
        this.status = TaskStatus.PENDING;
    }

    public Long getId() {
        return id;
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