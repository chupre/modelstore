package com.byllameister.modelstore.payments.yookassa;

import lombok.Data;

import java.util.UUID;

@Data
public class YookassaWebhookRequest {
    private String type;
    private String event;
    private PaymentObject object;

    @Data
    public static class PaymentObject {
        private UUID id;
        private String status;
        private Amount amount;

        @Data
        public static class Amount {
            private String value;
            private String currency;
        }
    }
}
