package com.byllameister.modelstore.products.interaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductCommentLikeMapper {
    @Mapping(target = "commentId", source = "comment.id")
    @Mapping(target = "userId", source = "user.id")
    ProductCommentLikeDto toDto(ProductCommentLike productCommentLike);
}
