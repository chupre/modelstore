package com.byllameister.modelstore.carts;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    List<CartDto> toDtos(List<Cart> carts);
}
