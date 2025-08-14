package com.byllameister.modelstore.payments.yookassa;

import com.byllameister.modelstore.orders.OrderStatus;
import com.byllameister.modelstore.orders.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook/yookassa")
@AllArgsConstructor
public class YookassaWebhookController {
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestBody YookassaWebhookRequest webhook) {
        var paymentId = webhook.getObject().getId();
        var status = webhook.getObject().getStatus();
        orderRepository.findByPaymentId(paymentId).ifPresent(order -> {
            switch (status) {
                case "succeeded" -> order.setStatus(OrderStatus.PAID);
                case "canceled"  -> order.setStatus(OrderStatus.CANCELED);
                default          -> order.setStatus(OrderStatus.PENDING);
            }
            orderRepository.save(order);
        });

        return ResponseEntity.ok().build();
    }
}
