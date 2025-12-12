package com.TaskManager.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TaskManager.DTO.TaskCreateRequest;
import com.TaskManager.DTO.TaskDTO;
import com.TaskManager.DTO.TaskUpdateRequest;
import com.TaskManager.Entity.Task;
import com.TaskManager.Entity.User;
import com.TaskManager.Exception.ResourceNotFoundException;
import com.TaskManager.Repository.TaskRepository;
import com.TaskManager.Repository.UserRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDTO toDto(Task t) {
        TaskDTO dto = new TaskDTO();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        dto.setStatus(t.getStatus());
        dto.setDueDate(t.getDueDate());
        dto.setOwnerId(t.getOwner().getId());
        dto.setOwnerName(t.getOwner().getName());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());
        return dto;
    }

    @Transactional
    public TaskDTO createTask(TaskCreateRequest req, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = new Task();
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setDueDate(req.getDueDate());
        task.setStatus(Task.Status.PENDING);
        task.setOwner(owner);

        task = taskRepository.save(task);
        return toDto(task);
    }

    public Page<TaskDTO> listTasksForUser(User requester, int page, int size, String statusFilter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Task> tasksPage;

        if (requester.getRole() == User.Role.ADMIN) {
            if (statusFilter == null) {
                tasksPage = taskRepository.findAll(pageable);
            } else {
                Task.Status status = Task.Status.valueOf(statusFilter.toUpperCase());
                tasksPage = taskRepository.findByStatus(status, pageable);
            }
        } else {
            if (statusFilter == null) {
                tasksPage = taskRepository.findByOwner(requester, pageable);
            } else {
                Task.Status status = Task.Status.valueOf(statusFilter.toUpperCase());
                tasksPage = taskRepository.findByOwnerAndStatus(requester, status, pageable);
            }
        }

        return tasksPage.map(this::toDto);
    }

    public TaskDTO getTaskById(Long id, User requester) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (!t.getOwner().getId().equals(requester.getId()) && requester.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Task not found"); // hide existence
        }

        return toDto(t);
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskUpdateRequest req, User requester) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!t.getOwner().getId().equals(requester.getId()) && requester.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Task not found");
        }

        if (req.getTitle() != null) t.setTitle(req.getTitle());
        if (req.getDescription() != null) t.setDescription(req.getDescription());
        if (req.getDueDate() != null) t.setDueDate(req.getDueDate());

        taskRepository.save(t);
        return toDto(t);
    }

    @Transactional
    public void deleteTask(Long id, User requester) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!t.getOwner().getId().equals(requester.getId()) && requester.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Task not found");
        }

        taskRepository.delete(t);
    }

    @Transactional
    public TaskDTO updateStatus(Long id, Task.Status status, User requester) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!t.getOwner().getId().equals(requester.getId()) && requester.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Task not found");
        }

        t.setStatus(status);
        taskRepository.save(t);
        return toDto(t);
    }
}
