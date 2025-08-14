package com.byllameister.modelstore.payments;

import java.math.BigDecimal;

public interface PaymentGatewayService {
    PaymentResponse createPayment(BigDecimal amount, Currency currency, String redirectUrl) throws PaymentResponseParseFailed;
}
