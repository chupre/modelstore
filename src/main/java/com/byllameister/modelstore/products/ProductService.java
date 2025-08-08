package com.byllameister.modelstore.products;

import com.byllameister.modelstore.common.PageableValidator;
import com.byllameister.modelstore.categories.CategoryNotFoundInBodyException;
import com.byllameister.modelstore.upload.UploadService;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.categories.CategoryRepository;
import com.byllameister.modelstore.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PageableValidator pageableValidator;
    private final UploadService uploadService;

    private final Set<String> VALID_SORT_FIELDS = Set.of("id", "title", "price", "categoryId", "createdAt");

    public Page<ProductDto> getProducts(
            String search,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);

        var products = productRepository.fuzzySearch(search, categoryId, minPrice, maxPrice, 0.2, pageable);
        return products.map(productMapper::toDtoFromFlatDto);
    }

    public ProductDto getProductById(Long id) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.toDto(product);
    }

    public ProductDto createProduct(CreateProductRequest request) throws IOException {
        var category = categoryRepository.findById(request.getCategoryId()).
                orElseThrow(CategoryNotFoundInBodyException::new);

        var owner = userRepository.findById(request.getOwnerId()).
                orElseThrow(UserNotFoundException::new);

        var imageFileUrl = uploadService.uploadImage(request.getPreviewImage());
        var modelFileUrl = uploadService.uploadImage(request.getFile());

        var product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setOwner(owner);
        product.setPreviewImage(imageFileUrl);
        product.setFile(modelFileUrl);
        productRepository.save(product);
        product.setId(product.getId());
        product.setCreatedAt(product.getCreatedAt());

        return productMapper.toDto(product);
    }

    public void deleteProductById(Long id) throws IOException {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        uploadService.deleteFile(product.getFile());
        uploadService.deleteFile(product.getPreviewImage());
        productRepository.delete(product);
    }

    public ProductDto updateProductById(Long id, UpdateProductRequest request) throws IOException {
        var category = categoryRepository.findById(request.getCategoryId()).
                orElseThrow(CategoryNotFoundInBodyException::new);

        var product = productRepository.findById(id).
                orElseThrow(ProductNotFoundException::new);

        uploadService.deleteFile(product.getPreviewImage());
        uploadService.deleteFile(product.getFile());
        var imageFileUrl = uploadService.uploadImage(request.getPreviewImage());
        var modelFileUrl = uploadService.uploadModel(request.getFile());

        productMapper.update(request, product);
        product.setCategory(category);
        product.setPreviewImage(imageFileUrl);
        product.setFile(modelFileUrl);
        productRepository.save(product);

        return productMapper.toDto(product);
    }
}
