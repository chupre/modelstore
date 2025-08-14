package com.byllameister.modelstore.users;

import com.byllameister.modelstore.auth.CustomUserPrincipal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role", length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "createdat", updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "verified")
    private Boolean verified;

    public static Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("User is not authenticated");
        }

        return ((CustomUserPrincipal) authentication.getPrincipal()).getUserId();
    }

    public static boolean isCurrentUserAdmin() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream().
                anyMatch(
                        a -> a.getAuthority().equals("ROLE_" + Role.ADMIN.name()));
    }
}