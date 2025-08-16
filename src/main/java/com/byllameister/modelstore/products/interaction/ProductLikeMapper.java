package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.users.LikedProductsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductLikeMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "userId", source = "user.id")
    ProductLikeDto toDto(ProductLike like);

    default Long toProductId(ProductLike like) {
        return like.getProduct().getId();
    }

    default LikedProductsResponse toResponse(List<ProductLike> likes) {
        List<Long> ids = likes.stream()
                .map(this::toProductId)
                .toList();
        return new LikedProductsResponse(ids);
    }
}
