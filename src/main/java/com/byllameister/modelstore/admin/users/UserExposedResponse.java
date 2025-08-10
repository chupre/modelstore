package com.byllameister.modelstore.admin.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserExposedResponse {
    private Long id;
    private String username;
    private String email;
}
