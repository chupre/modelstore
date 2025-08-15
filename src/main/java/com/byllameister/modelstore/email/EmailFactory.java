package com.byllameister.modelstore.email;

import com.byllameister.modelstore.users.emailVerification.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailFactory {
    @Value("${spring.cors.frontendUrl}")
    private String frontendUrl;

    @Value("${user.verificationToken.durationMinutes}")
    private int verificationTokenDurationMinutes;

    @Value("${user.passwordResetToken.durationMinutes}")
    private long passwordResetTokenDurationMinutes;

    public Email createAccountVerificationEmail(VerificationToken verificationToken) {
        String verificationLink = String.format("%s/home/?verification=%s", frontendUrl, verificationToken.getId());

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
        String passwordResetLink = String.format("%s/login?passwordReset=%s", frontendUrl, rawToken);

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
