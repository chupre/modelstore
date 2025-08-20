package com.byllameister.modelstore.payments.yookassa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YookassaPaymentRequest {
    private YookassaAmount amount;
    private Confirmation confirmation;
    private Boolean capture;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Confirmation {
        private String type;
        private String return_url;
    }
}
