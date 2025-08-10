package com.byllameister.modelstore.admin.carts;

import com.byllameister.modelstore.admin.users.UserExposedResponse;
import com.byllameister.modelstore.carts.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CartExposedResponse {
    private UUID id;
    private UserExposedResponse user;
    private List<CartItemDto> cartItems;
    private BigDecimal totalPrice;
}
