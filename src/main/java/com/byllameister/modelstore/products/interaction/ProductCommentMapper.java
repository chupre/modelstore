package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.users.LikedCommentsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductCommentMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "userId", source = "user.id")
    ProductCommentDto toDto(ProductComment comment);

    default Long toCommentId(ProductCommentLike like) {
        return like.getComment().getId();
    }

    default LikedCommentsResponse toResponse(List<ProductCommentLike> likes) {
        List<Long> ids = likes.stream()
                .map(this::toCommentId)
                .toList();
        return new LikedCommentsResponse(ids);
    }
}
