package com.byllameister.modelstore.payments;

import com.byllameister.modelstore.sellers.Seller;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentResponse createPayment(BigDecimal amount, String redirectUrl) throws PaymentResponseParseFailed;
    PayoutResponse createPayout(BigDecimal amount, PayoutDestination payoutDestination);
    PayoutDestination buildPayoutDestination(Seller seller);
}
