package com.byllameister.modelstore.payments;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PayoutDto {
    Long id;
    Long sellerId;
    Long orderId;
    BigDecimal amount;
    PayoutStatus status;
    String gatewayPaymentId;
    Instant createdAt;
}
