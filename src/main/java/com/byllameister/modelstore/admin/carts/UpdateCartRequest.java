package com.byllameister.modelstore.admin.carts;

import com.byllameister.modelstore.carts.CartItemDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateCartRequest {
    @NotNull(message = "cartItems must be provided")
    private Set<CartItemDto> cartItems;
}
