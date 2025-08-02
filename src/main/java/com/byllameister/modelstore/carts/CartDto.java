package com.byllameister.modelstore.carts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CartDto {
    private UUID id;
    private Long userId;
    private List<CartItemDto> cartItems;
    private BigDecimal totalPrice;
}
