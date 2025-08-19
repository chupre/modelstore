package com.byllameister.modelstore.orders;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customerId", source = "customer.id")
    OrderResponse toDto(Order order);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "product.id", source = "product.id")
    OrderItemDto toDto(OrderItem item);
}
