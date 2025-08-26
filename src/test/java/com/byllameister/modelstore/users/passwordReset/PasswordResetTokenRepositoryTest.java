package com.byllameister.modelstore.users.passwordReset;
import com.byllameister.modelstore.users.User;
import com.byllameister.modelstore.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PasswordResetTokenRepositoryTest {

    @Autowired
    private PasswordResetTokenRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByTokenHash_shouldReturnToken() {
        var user = new User();
        userRepository.save(user);

        PasswordResetToken token = new PasswordResetToken();
        token.setTokenHash("abc123");
        token.setUser(user);
        token.setExpiresAt(Instant.now().plusSeconds(3600));
        repository.save(token);

        Optional<PasswordResetToken> result = repository.findByTokenHash("abc123");

        assertThat(result).isPresent();
        assertThat(result.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(result.get().getTokenHash()).isEqualTo("abc123");
    }

    @Test
    void findByTokenHash_shouldReturnEmptyWhenNotFound() {
        Optional<PasswordResetToken> result = repository.findByTokenHash("nonexistent");
        assertThat(result).isNotPresent();
    }
}
