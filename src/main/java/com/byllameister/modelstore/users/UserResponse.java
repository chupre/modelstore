package com.byllameister.modelstore.users;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
}
