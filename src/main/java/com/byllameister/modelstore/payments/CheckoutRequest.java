package com.byllameister.modelstore.payments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "cartId is required")
    private UUID cartId;

    @NotBlank(message = "redirectUrl must be not blank")
    private String redirectUrl;
}
