package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Optional<Cart> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE CartItem ci SET ci.isSelected = true WHERE ci.cart.id = :cartId")
    void selectAllItemsByCartId(@Param("cartId") UUID cartId);

    @Modifying
    @Query("UPDATE CartItem ci SET ci.isSelected = false WHERE ci.cart.id = :cartId")
    void unselectAllItemsByCartId(@Param("cartId") UUID cartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.isSelected = true AND ci.cart.id = :id")
    void deleteAllSelectedItems(@Param("id") UUID id);
}