package com.byllameister.modelstore.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    CartDto toDto(Cart cart);

    List<CartDto> toDtos(List<Cart> carts);
}
