package com.byllameister.modelstore.users.profiles;

import lombok.Data;

@Data
public class UserProfileDto {
    private Long id;
    private Long userId;
    private String name;
    private String avatarUrl;
    private String bio;
}
