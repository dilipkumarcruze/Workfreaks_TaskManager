package com.TaskManager.Controller;

import com.TaskManager.DTO.UserRequest;
import com.TaskManager.DTO.UserResponse;
import com.TaskManager.Entity.User;
import com.TaskManager.Service.UserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE USER
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        User user = userService.createUser(request);
        return new UserResponse(user);
    }

    // READ USER
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return new UserResponse(user);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        User user = userService.updateUser(id, request);
        return new UserResponse(user);
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "User deleted successfully";
    }
}
