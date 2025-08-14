package com.byllameister.modelstore.orders;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class OrderDto {
    private Long id;
    private Long customerId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private Set<OrderItemDto> orderItems;
    private UUID paymentId;
    private Instant createdAt;
}
