package com.byllameister.modelstore.products;

import com.byllameister.modelstore.common.PageableValidator;
import com.byllameister.modelstore.categories.CategoryNotFoundInBodyException;
import com.byllameister.modelstore.categories.CategoryNotFoundInQueryException;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.categories.CategoryRepository;
import com.byllameister.modelstore.users.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<ProductDto> getAllProducts(String search, Pageable pageable) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);

        if (search != null && !search.isBlank()) {
            var products = productRepository.fuzzySearch(search, 0.3, pageable);
            return products.map(productMapper::toDtoFromFlatDto);
        } else {
            var products = productRepository.findAll(pageable);
            return products.map(productMapper::toDto);
        }
    }

    public ProductDto getProductById(Long id) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.toDto(product);
    }

    public Page<ProductDto> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundInQueryException();
        }

        var products = productRepository.findProductsByCategoryId(categoryId, pageable);
        return products.map(productMapper::toDto);
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
