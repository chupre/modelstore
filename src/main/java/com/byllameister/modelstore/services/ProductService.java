package com.byllameister.modelstore.services;

import com.byllameister.modelstore.dtos.ProductDto;
import com.byllameister.modelstore.entities.Product;
import com.byllameister.modelstore.exceptions.ProductNotFoundException;
import com.byllameister.modelstore.mappers.ProductMapper;
import com.byllameister.modelstore.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getAllProducts() {
        var products = productRepository.findAll();
        return productMapper.toDtos(products);
    }

    public ProductDto getProductById(Long id) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        product.setCategory(product.getCategory());
        return productMapper.toDto(product);
    }
}
