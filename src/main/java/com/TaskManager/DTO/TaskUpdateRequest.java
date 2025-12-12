package com.TaskManager.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
}
