package com.byllameister.modelstore.admin.users;

import com.byllameister.modelstore.users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UserExposedResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Boolean verified;
    private LocalDate createdAt;
}
