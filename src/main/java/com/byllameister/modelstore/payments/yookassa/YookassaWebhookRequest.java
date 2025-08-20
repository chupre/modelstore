package com.byllameister.modelstore.payments.yookassa;

import lombok.Data;

@Data
public class YookassaWebhookRequest {
    private String type;
    private String event;
    private PaymentObject object;

    @Data
    public static class PaymentObject {
        private String id;
        private String status;
        private Amount amount;

        @Data
        public static class Amount {
            private String value;
            private String currency;
        }
    }
}
