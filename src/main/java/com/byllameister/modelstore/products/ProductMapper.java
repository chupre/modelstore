package com.byllameister.modelstore.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "ownerId", source = "owner.id")
    ProductDto toDto(Product product);

    List<ProductDto> toDtos(Iterable<Product> products);

    Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);
}
