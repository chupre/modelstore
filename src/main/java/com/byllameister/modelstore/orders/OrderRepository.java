package com.byllameister.modelstore.orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByPaymentId(UUID paymentId);

    Page<Order> findByCustomerId(Long currentUserId, Pageable pageable);

    @Query("""
            select (count(o) > 0) from Order o inner join o.orderItems orderItems
            where o.customer.id = :customerId and o.status = "PAID" and orderItems.product.id = :productId""")
    boolean hasUserBoughtProduct(@Param("customerId") Long customerId, @Param("productId") Long productId);

    @NonNull
    @EntityGraph(attributePaths = "orderItems")
    Page<Order> findAll(@NonNull Pageable pageable);
}