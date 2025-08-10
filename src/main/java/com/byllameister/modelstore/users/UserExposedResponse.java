package com.byllameister.modelstore.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserExposedResponse {
    private Long id;
    private String username;
    private String email;
}
