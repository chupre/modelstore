package com.byllameister.modelstore.products;

import com.byllameister.modelstore.categories.CategoryDto;
import com.byllameister.modelstore.users.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);

    @Mapping(target = "previewImage", ignore = true)
    @Mapping(target = "file", ignore = true)
    Product toEntity(CreateProductRequest createProductRequest);

    @Mapping(target = "previewImage", ignore = true)
    @Mapping(target = "file", ignore = true)
    void update(UpdateProductRequest request, @MappingTarget Product product);

    @Mapping(target = "owner", expression = "java(mapUser(productFlatDto))")
    @Mapping(target = "category", expression = "java(mapCategory(productFlatDto))")
    ProductDto toDtoFromFlatDto(ProductFlatDto productFlatDto);

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
}
