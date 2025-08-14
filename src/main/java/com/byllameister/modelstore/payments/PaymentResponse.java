package com.byllameister.modelstore.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponse {
    private final String paymentId;
    private final String confirmationUrl;
}