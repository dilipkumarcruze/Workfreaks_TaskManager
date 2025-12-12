package com.TaskManager.Controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TaskManager.DTO.StatusUpdateRequest;
import com.TaskManager.DTO.TaskCreateRequest;
import com.TaskManager.DTO.TaskDTO;
import com.TaskManager.DTO.TaskUpdateRequest;
import com.TaskManager.Entity.Task;
import com.TaskManager.Entity.User;
import com.TaskManager.Service.TaskService;
import com.TaskManager.Service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    private User currentUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername());
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@AuthenticationPrincipal UserDetails userDetails,
                                              @Valid @RequestBody TaskCreateRequest req) {
        User u = currentUser(userDetails);
        TaskDTO dto = taskService.createTask(req, u.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<Page<TaskDTO>> listTasks(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size,
                                                   @RequestParam(required = false) String status) {
        User u = currentUser(userDetails);
        Page<TaskDTO> p = taskService.listTasksForUser(u, page, size, status);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable Long id) {
        User u = currentUser(userDetails);
        TaskDTO dto = taskService.getTaskById(id, u);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long id,
                                              @Valid @RequestBody TaskUpdateRequest req) {
        User u = currentUser(userDetails);
        TaskDTO dto = taskService.updateTask(id, req, u);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateStatus(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable Long id,
                                                @Valid @RequestBody StatusUpdateRequest req) {
        User u = currentUser(userDetails);
        Task.Status status = Task.Status.valueOf(req.getStatus().toUpperCase());
        TaskDTO dto = taskService.updateStatus(id, status, u);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable Long id) {
        User u = currentUser(userDetails);
        taskService.deleteTask(id, u);
        return ResponseEntity.noContent().build();
    }
}
