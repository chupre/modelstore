package com.byllameister.modelstore.orders;

import com.byllameister.modelstore.payments.CheckoutService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderPaidEventListener {
    private final CheckoutService checkoutService;

    @Async
    @EventListener
    public void handleOrderPaid(OrderPaidEvent event) {
        checkoutService.payout(event.getOrder());
    }
}
