package com.byllameister.modelstore.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);

    List<ProductDto> toDtos(Iterable<Product> products);

    Product toEntity(ProductDto productDto);
    Product toEntity(CreateProductRequest createProductRequest);

    void update(UpdateProductRequest request, @MappingTarget Product product);

    ProductDto toDtoFromFlatDto(ProductFlatDto productFlatDto);
}
