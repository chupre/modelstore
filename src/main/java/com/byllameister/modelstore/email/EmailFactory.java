package com.byllameister.modelstore.email;

import com.byllameister.modelstore.users.verification.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailFactory {
    @Value("${server.url}")
    private String serverUrl;

    @Value("${user.verificationToken.durationMinutes}")
    private int verificationTokenDurationMinutes;

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
}
