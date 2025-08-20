package com.byllameister.modelstore.payments;

public class PayoutNotFoundException extends RuntimeException {
    public PayoutNotFoundException() {
        super("Payout not found");
    }
}
