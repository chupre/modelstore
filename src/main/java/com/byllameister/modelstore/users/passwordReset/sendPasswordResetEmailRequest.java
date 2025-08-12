package com.byllameister.modelstore.users.passwordReset;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class sendPasswordResetEmailRequest {
    @NotBlank(message = "Email must be provided")
    @Email(message = "Email is invalid")
    private String email;
}
