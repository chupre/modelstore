package com.byllameister.modelstore.payments;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayoutMapper {
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "paymentId", target = "gatewayPaymentId")
    PayoutDto toDto(Payout payout);
}
