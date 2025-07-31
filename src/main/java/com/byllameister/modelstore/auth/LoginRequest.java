package com.byllameister.modelstore.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequest {
    @NotBlank(message = "Email must be provided")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password must be provided")
    private String password;
}
