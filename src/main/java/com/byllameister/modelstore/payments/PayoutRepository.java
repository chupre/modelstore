package com.byllameister.modelstore.payments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayoutRepository extends JpaRepository<Payout, Long> {
    Optional<Payout> findByPaymentId(String paymentId);
}
