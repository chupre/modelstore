package com.byllameister.modelstore.users.passwordReset;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PasswordResetTokenResponse {
    private Long id;
    private Long userId;
    private String tokenHash;
    private Instant expiresAt;
}
