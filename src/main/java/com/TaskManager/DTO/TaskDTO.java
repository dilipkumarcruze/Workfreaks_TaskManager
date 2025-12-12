package com.TaskManager.DTO;

import java.time.Instant;
import java.time.LocalDate;

import com.TaskManager.Entity.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Task.Status status;
    private LocalDate dueDate;
    private Long ownerId;
    private String ownerName;
    private Instant createdAt;
    private Instant updatedAt;
}
