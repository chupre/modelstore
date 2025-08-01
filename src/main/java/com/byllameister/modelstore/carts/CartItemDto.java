package com.byllameister.modelstore.carts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private UUID cartId;
    private Long productId;
}
