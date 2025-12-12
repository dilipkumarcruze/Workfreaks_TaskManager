package com.TaskManager.Entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.PENDING;

    private LocalDate dueDate;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User owner;

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

    // No-args constructor
    public Task() {}

    // All-args constructor
    public Task(Long id, String title, String description, Status status, LocalDate dueDate,
                Instant createdAt, Instant updatedAt, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    @Override
    public String toString() {
        return "Task [id=" + id + ", title=" + title + ", description=" + description
                + ", status=" + status + ", dueDate=" + dueDate + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", owner=" + owner + "]";
    }
}
