package com.byllameister.modelstore.users.passwordReset;

public class PasswordResetTokenExpiredException extends RuntimeException {
    public PasswordResetTokenExpiredException() {
        super("Password reset token has expired");
    }
}
