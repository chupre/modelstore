package com.byllameister.modelstore.payments;

import lombok.Getter;

@Getter
public enum PayoutMethod {
    BANK_CARD("bank_card"),
    YOOMONEY_WALLET("yoo_money");

    private final String method;

    PayoutMethod(String method) {
        this.method = method;
    }
}
