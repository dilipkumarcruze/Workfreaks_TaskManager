package com.TaskManager.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.TaskManager.Entity.Task;
import com.TaskManager.Entity.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByOwner(User owner, Pageable pageable);
    Page<Task> findByStatus(Task.Status status, Pageable pageable);
    Page<Task> findByOwnerAndStatus(User owner, Task.Status status, Pageable pageable);
    List<Task> findByOwner(User owner);
}
