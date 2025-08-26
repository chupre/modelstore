package com.byllameister.modelstore.users;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "profile")
    Optional<User> findByEmail(String email);

    @Override
    @Nonnull
    @EntityGraph(attributePaths = "profile")
    Page<User> findAll(@NonNull Pageable pageable);

    @Override
    @Nonnull
    @EntityGraph(attributePaths = "profile")
    Optional<User> findById(@NonNull Long id);
}
