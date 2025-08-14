package com.byllameister.modelstore.payments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "cartId is required")
    private UUID cartId;

    @NotNull(message = "currency is required. Supported: RUB, EUR, USD")
    private Currency currency;

    @NotBlank
    private String redirectUrl;
}
