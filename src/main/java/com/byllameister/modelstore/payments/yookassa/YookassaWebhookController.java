package com.byllameister.modelstore.payments.yookassa;

import com.byllameister.modelstore.orders.OrderNotFoundException;
import com.byllameister.modelstore.orders.OrderStatus;
import com.byllameister.modelstore.orders.OrderRepository;
import com.byllameister.modelstore.orders.OrderPaidEvent;
import com.byllameister.modelstore.payments.PayoutNotFoundException;
import com.byllameister.modelstore.payments.PayoutRepository;
import com.byllameister.modelstore.payments.PayoutStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Yookassa Webhooks")
@RequestMapping("/webhook/yookassa")
@AllArgsConstructor
public class YookassaWebhookController {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PayoutRepository payoutRepository;

    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestBody YookassaWebhookRequest webhook) {
        var paymentId = webhook.getObject().getId();
        var status = webhook.getObject().getStatus();
        var order = orderRepository.findByPaymentId(UUID.fromString(paymentId)).orElseThrow(OrderNotFoundException::new);
        switch (status) {
            case "succeeded" -> {
                order.setStatus(OrderStatus.PAID);
                applicationEventPublisher.publishEvent(new OrderPaidEvent(order));
            }
            case "canceled"  -> order.setStatus(OrderStatus.CANCELLED);
            default          -> order.setStatus(OrderStatus.PENDING);
        }
        orderRepository.save(order);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/payout")
    public ResponseEntity<Void> handlePayoutWebhook(@RequestBody YookassaWebhookRequest webhook) {
        var paymentId = webhook.getObject().getId();
        var status = webhook.getObject().getStatus();
        var payout = payoutRepository.findByPaymentId(paymentId).orElseThrow(PayoutNotFoundException::new);

        switch (status) {
            case "succeeded" -> payout.setStatus(PayoutStatus.PAID);
            case "canceled"  -> payout.setStatus(PayoutStatus.CANCELED);
            default          -> payout.setStatus(PayoutStatus.PENDING);
        }

        payoutRepository.save(payout);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> handleOrderNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(PayoutNotFoundException.class)
    public ResponseEntity<Void> handlePayoutNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
