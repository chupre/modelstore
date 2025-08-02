package com.byllameister.modelstore.carts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateCartRequest {
    private Set<CartItemDto> cartItems;
}
