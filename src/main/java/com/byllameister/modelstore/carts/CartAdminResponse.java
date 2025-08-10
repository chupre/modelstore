package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.users.UserAdminResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CartAdminResponse {
    private UUID id;
    private UserAdminResponse user;
    private List<CartItemDto> cartItems;
    private BigDecimal totalPrice;
}
