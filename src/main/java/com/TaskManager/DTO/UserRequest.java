package com.TaskManager.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String name;
    private String email;
    private String password; // plaintext for incoming request
    private String avatarUrl;
}
