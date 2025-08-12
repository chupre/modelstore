package com.byllameister.modelstore.users.emailVerification;

import com.byllameister.modelstore.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "verification_tokens")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expires_at")
    private Instant expiresAt;

    public void validate() {
        if (expiresAt.isBefore(Instant.now())) {
            throw new VerificationTokenExpiredException();
        }

        if (!user.getId().equals(User.getCurrentUserId())) {
            throw new VerificationForbiddenException();
        }
    }
}