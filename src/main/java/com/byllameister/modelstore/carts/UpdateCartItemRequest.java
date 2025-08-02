package com.byllameister.modelstore.carts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "productId must be provided")
    private Long productId;
}
