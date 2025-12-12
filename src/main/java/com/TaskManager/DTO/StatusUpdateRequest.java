package com.TaskManager.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    @NotNull
    private String status; // Accepts: PENDING, IN_PROGRESS, COMPLETED
}
