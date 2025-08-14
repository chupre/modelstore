package com.byllameister.modelstore.payments.yookassa;

import com.byllameister.modelstore.payments.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YookassaPaymentRequest {
    private Amount amount;
    private Confirmation confirmation;
    private Boolean capture;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Amount {
        private String value;
        private Currency currency;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Confirmation {
        private String type;
        private String return_url;
    }
}
