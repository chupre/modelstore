package com.byllameister.modelstore.payments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayoutRepository extends JpaRepository<Payout, Long> {
    Optional<Payout> findByPaymentId(String paymentId);

    @EntityGraph(attributePaths = {"order", "seller"})
    Page<Payout> findAllBySellerUserId(Long userId, Pageable pageable);
}
