package com.byllameister.modelstore.products.interaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductCommentMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "likes", expression = "java((long) comment.getLikes().size())")
    ProductCommentDto toDto(ProductComment comment);
}
