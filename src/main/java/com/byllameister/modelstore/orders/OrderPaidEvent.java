package com.byllameister.modelstore.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderPaidEvent {
    private Order order;
}
