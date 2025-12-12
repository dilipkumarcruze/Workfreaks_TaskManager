package com.TaskManager.DTO;

import com.TaskManager.Entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String avatarUrl;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.avatarUrl = user.getAvatarUrl();
    }
}
