package com.byllameister.modelstore.products.interaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductCommentMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "userId", source = "user.id")
    ProductCommentDto toDto(ProductComment comment);
}
