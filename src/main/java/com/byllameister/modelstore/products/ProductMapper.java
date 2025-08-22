package com.byllameister.modelstore.products;

import com.byllameister.modelstore.categories.CategoryDto;
import com.byllameister.modelstore.users.UserDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);

    @Mapping(target = "previewImage", ignore = true)
    @Mapping(target = "file", ignore = true)
    Product toEntity(CreateProductDto createProductRequest);

    @Mapping(target = "previewImage", ignore = true)
    @Mapping(target = "file", ignore = true)
    void update(UpdateProductRequest request, @MappingTarget Product product);

    @Mapping(target = "previewImage", ignore = true)
    @Mapping(target = "file", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(PatchProductRequest request, @MappingTarget Product product);

    @Mapping(target = "owner", expression = "java(mapUser(productFlatDto))")
    @Mapping(target = "category", expression = "java(mapCategory(productFlatDto))")
    ProductWithLikesResponse toDtoFromFlatDto(ProductFlatDto productFlatDto);

    @Mapping(target = "owner", expression = "java(mapUser(productFlatDto))")
    @Mapping(target = "category", expression = "java(mapCategory(productFlatDto))")
    ProductWithUserLikeResponse toDtoFromFlatDto(ProductWithUserLikeFlatDto productFlatDto);

    @Mapping(target = "owner", expression = "java(mapUser(productFlatDto))")
    @Mapping(target = "category", expression = "java(mapCategory(productFlatDto))")
    @Mapping(target = "revenue", expression = "java(formatRevenue(productFlatDto.getRevenue()))")
    ProductWithSellerStatsResponse toDtoFromFlatDto(ProductWithSellerStatsFlatDto productFlatDto);

    default Double formatRevenue(Double revenue) {
        if (revenue == null) return 0.0;
        return Math.round(revenue * 100.0) / 100.0; // keeps 2 decimals
    }

    default UserDto mapUser(ProductFlatDto productFlatDto) {
        return new UserDto(
                productFlatDto.getOwnerId(),
                productFlatDto.getOwnerUsername()
        );
    }

    default CategoryDto mapCategory(ProductFlatDto productFlatDto) {
        return new CategoryDto(
                productFlatDto.getCategoryId(),
                productFlatDto.getCategoryName()
        );
    }

    default UserDto mapUser(ProductWithUserLikeFlatDto productFlatDto) {
        return new UserDto(
                productFlatDto.getOwnerId(),
                productFlatDto.getOwnerUsername()
        );
    }

    default CategoryDto mapCategory(ProductWithUserLikeFlatDto productFlatDto) {
        return new CategoryDto(
                productFlatDto.getCategoryId(),
                productFlatDto.getCategoryName()
        );
    }
}
