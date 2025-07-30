package com.byllameister.modelstore.services;

import com.byllameister.modelstore.common.PageableValidator;
import com.byllameister.modelstore.dtos.ProductDto;
import com.byllameister.modelstore.exceptions.CategoryNotFoundInBodyException;
import com.byllameister.modelstore.exceptions.CategoryNotFoundInQueryException;
import com.byllameister.modelstore.exceptions.ProductNotFoundException;
import com.byllameister.modelstore.exceptions.UserNotFoundException;
import com.byllameister.modelstore.mappers.ProductMapper;
import com.byllameister.modelstore.repositories.CategoryRepository;
import com.byllameister.modelstore.repositories.ProductRepository;
import com.byllameister.modelstore.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PageableValidator pageableValidator;

    private final Set<String> VALID_SORT_FIELDS = Set.of("id", "title", "price", "categoryId");

    public List<ProductDto> getAllProducts(Pageable pageable) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);
        var products = productRepository.findAll(pageable);
        return productMapper.toDtos(products.getContent());
    }

    public ProductDto getProductById(Long id) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.toDto(product);
    }

    public List<ProductDto> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundInQueryException();
        }

        var products = productRepository.findProductsByCategoryId(categoryId, pageable);
        return productMapper.toDtos(products);
    }

    public ProductDto createProduct(ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId()).
                orElseThrow(CategoryNotFoundInBodyException::new);

        var owner = userRepository.findById(productDto.getOwnerId()).
                orElseThrow(UserNotFoundException::new);

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        product.setOwner(owner);
        productRepository.save(product);
        product.setId(product.getId());

        return productMapper.toDto(product);
    }

    public void deleteProductById(Long id) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }

    public ProductDto updateProductById(Long id, @Valid ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId()).
                orElseThrow(CategoryNotFoundInBodyException::new);

        var owner = userRepository.findById(productDto.getOwnerId()).
                orElseThrow(UserNotFoundException::new);

        var product = productRepository.findById(id).
                orElseThrow(ProductNotFoundException::new);

        productMapper.update(productDto, product);
        product.setCategory(category);
        product.setOwner(owner);
        productRepository.save(product);

        return productMapper.toDto(product);
    }
}
