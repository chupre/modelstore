package com.byllameister.modelstore.payments.yookassa;

import com.byllameister.modelstore.payments.PayoutDestination;
import com.byllameister.modelstore.payments.PayoutMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YookassaBankCard implements PayoutDestination {
    private final String type = PayoutMethod.BANK_CARD.getMethod();
    private BankCard card;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BankCard {
        private String number;
    }
}
