package com.byllameister.modelstore.admin.carts;

import com.byllameister.modelstore.carts.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateCartRequest {
    private Set<CartItemDto> cartItems;
}
