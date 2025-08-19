package com.byllameister.modelstore.orders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long orderId;
    private OrderProductDto product;
    private BigDecimal price;
}
