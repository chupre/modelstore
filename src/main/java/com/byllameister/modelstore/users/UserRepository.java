package com.byllameister.modelstore.users;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "profile")
    Optional<User> findByEmail(String email);

    Long id(Long id);
}
