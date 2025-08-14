package com.byllameister.modelstore.users.passwordReset;

import com.byllameister.modelstore.email.EmailFactory;
import com.byllameister.modelstore.email.EmailService;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailFactory emailFactory;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetTokenMapper passwordResetTokenMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${user.passwordResetToken.durationMinutes}")
    private long durationMinutes;

    public void sendPasswordResetEmail(sendPasswordResetEmailRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        var rawToken = PasswordResetTokenUtils.generateRawToken();
        var hashToken = PasswordResetTokenUtils.hashToken(rawToken);

        var token = new PasswordResetToken();
        token.setUser(user);
        token.setTokenHash(hashToken);
        token.setExpiresAt(Instant.now().plus(Duration.ofMinutes(durationMinutes)));
        passwordResetTokenRepository.save(token);

        var email = emailFactory.createPasswordResetEmail(user.getEmail(), rawToken);
        emailService.sendSimpleEmail(email);
    }

    public PasswordResetTokenResponse validate(String rawToken) {
        var hashToken = PasswordResetTokenUtils.hashToken(rawToken);
        var token = passwordResetTokenRepository.findByTokenHash(hashToken)
                .orElseThrow(PasswordResetTokenNotFoundException::new);

        if (token.isExpired()) {
            throw new PasswordResetTokenExpiredException();
        }

        return passwordResetTokenMapper.toDto(token);
    }

    public void changePassword(PasswordResetRequest request) {
        var token = validate(request.getToken());
        var user = userRepository.findById(token.getUserId()).orElseThrow(UserNotFoundException::new);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setVerified(true);
        userRepository.save(user);
        passwordResetTokenRepository.deleteById(token.getId());
    }
}
