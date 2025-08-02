package com.byllameister.modelstore.carts;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCartRequest {
    @NotNull(message = "userId must be provided")
    private Long userId;
}
