package com.byllameister.modelstore.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentGatewayResponse {
    private final String id;
    private final String confirmationUrl;
}