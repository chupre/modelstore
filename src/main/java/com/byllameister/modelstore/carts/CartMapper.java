package com.byllameister.modelstore.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    List<CartDto> toDtos(List<Cart> carts);

    void update(UpdateCartRequest request, @MappingTarget Cart cart);

    CartItemDto toDto(CartItem cartItem);
}
