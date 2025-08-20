package com.byllameister.modelstore.payments;

import com.byllameister.modelstore.orders.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderPaidEvent {
    private Order order;
}
