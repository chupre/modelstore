package com.byllameister.modelstore.payments;

public class PaymentResponseParseFailed extends RuntimeException {
    public PaymentResponseParseFailed(String message, Exception e) {
        super(message, e);
    }
}
