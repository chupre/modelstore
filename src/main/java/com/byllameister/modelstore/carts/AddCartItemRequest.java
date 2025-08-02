package com.byllameister.modelstore.carts;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCartItemRequest {
    @NotNull
    private Long product;
}
