package com.byllameister.modelstore.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartExposedResponse toCartExposedResponse(Cart cart);

    void update(UpdateCartRequest request, @MappingTarget Cart cart);

    CartItemDto toDto(CartItem cartItem);
}
