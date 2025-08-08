package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    boolean existsByUser(User user);

    @NonNull
    @Override
    @EntityGraph(value = "Cart.withAll")
    Page<Cart> findAll(@NonNull Pageable pageable);

    @NonNull
    @Override
    @EntityGraph(value = "Cart.withAll")
    Optional<Cart> findById(@NonNull UUID id);

    @EntityGraph(value = "Cart.withAll")
    Optional<Cart> findByUser(User user);
}