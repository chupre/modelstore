package com.byllameister.modelstore.users.verification;

import com.byllameister.modelstore.email.EmailFactory;
import com.byllameister.modelstore.email.EmailService;
import com.byllameister.modelstore.users.User;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserVerificationService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailFactory emailFactory;

    @Value("${user.verificationToken.durationMinutes}")
    private long durationMinutes;

    public void sendVerificationEmail() {
        var token = createVerificationToken(User.getCurrentUserId());
        var email = emailFactory.createAccountVerificationEmail(token);
        emailService.sendSimpleEmail(email);
    }

    public void verify(UUID verificationToken) {
        var token = verificationTokenRepository.findById(verificationToken)
                .orElseThrow(VerificationTokenNotFoundException::new);

        token.validate();
        var user = token.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verificationTokenRepository.deleteById(verificationToken);
    }

    private VerificationToken createVerificationToken(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        var token = new VerificationToken();
        token.setUser(user);
        token.setExpiresAt(Instant.now().plus(Duration.ofMinutes(durationMinutes)));
        return verificationTokenRepository.save(token);
    }
}
