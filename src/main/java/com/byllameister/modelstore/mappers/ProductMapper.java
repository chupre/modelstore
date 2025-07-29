package com.byllameister.modelstore.mappers;

import com.byllameister.modelstore.dtos.ProductDto;
import com.byllameister.modelstore.entities.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    List<ProductDto> toDtos(Iterable<Product> products);
}
