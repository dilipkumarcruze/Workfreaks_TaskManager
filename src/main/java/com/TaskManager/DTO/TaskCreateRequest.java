package com.TaskManager.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreateRequest {
    @NotBlank
    private String title;
    private String description;
    private LocalDate dueDate;
}
