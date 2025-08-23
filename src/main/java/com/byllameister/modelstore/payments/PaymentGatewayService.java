package com.byllameister.modelstore.payments;

import com.byllameister.modelstore.sellers.Seller;

import java.math.BigDecimal;

public interface PaymentGatewayService {
    PaymentGatewayResponse createPayment(BigDecimal amount, String redirectUrl) throws PaymentResponseParseFailed;
    PayoutGatewayResponse createPayout(BigDecimal amount, PayoutDestination payoutDestination);
    PayoutDestination buildPayoutDestination(Seller seller);
}
