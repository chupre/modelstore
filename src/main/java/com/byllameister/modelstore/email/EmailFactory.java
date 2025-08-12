package com.byllameister.modelstore.email;

import com.byllameister.modelstore.users.emailVerification.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailFactory {
    @Value("${server.url}")
    private String serverUrl;

    @Value("${user.verificationToken.durationMinutes}")
    private int verificationTokenDurationMinutes;

    @Value("${user.passwordResetToken.durationMinutes}")
    private long passwordResetTokenDurationMinutes;

    public Email createAccountVerificationEmail(VerificationToken verificationToken) {
        String verificationLink = String.format("%s/users/verification/%s", serverUrl, verificationToken.getId());

        String subject = "Verify Your Account";
        String body = String.format(
                """
                        Please verify your account by clicking the link below:
                        %s
                        
                        This link will expire in %d minutes.
                        
                        If you did not request this, please ignore this email.
                        """,
                verificationLink, verificationTokenDurationMinutes
        );

        return new Email(verificationToken.getUser().getEmail(), subject, body);
    }

    public Email createPasswordResetEmail(String recipientEmail, String rawToken) {
        String passwordResetLink = String.format("%s/users/passwordReset/%s", serverUrl, rawToken);

        String subject = "Reset Password";
        String body = String.format(
                """
                        Change your password by clicking the link below:
                        %s
                        
                        This link will expire in %d minutes.
                        
                        If you did not request this, please ignore this email.
                        """,
                passwordResetLink, passwordResetTokenDurationMinutes
        );

        return new Email(recipientEmail, subject, body);
    }
}
