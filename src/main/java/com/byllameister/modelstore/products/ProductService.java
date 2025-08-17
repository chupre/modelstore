package com.byllameister.modelstore.products;

import com.byllameister.modelstore.admin.products.AdminCreateProductRequest;
import com.byllameister.modelstore.admin.products.ForbiddenOwnerRoleException;
import com.byllameister.modelstore.categories.Category;
import com.byllameister.modelstore.categories.CategoryNotFoundInBodyException;
import com.byllameister.modelstore.common.PageableUtils;
import com.byllameister.modelstore.sellers.SellerNotFoundException;
import com.byllameister.modelstore.sellers.SellerRepository;
import com.byllameister.modelstore.upload.UploadService;
import com.byllameister.modelstore.users.Role;
import com.byllameister.modelstore.users.User;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.categories.CategoryRepository;
import com.byllameister.modelstore.users.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UploadService uploadService;

    private final SellerRepository sellerRepository;

    public Page<ProductWithLikesResponse> getProducts(
            String search,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        PageableUtils.validate(pageable, PageableUtils.PRODUCT_SORT_FIELDS);

        var products = productRepository.fuzzySearch(search, categoryId, minPrice, maxPrice, 0.2, pageable);
        return products.map(productMapper::toDtoFromFlatDto);
    }

    public Page<ProductWithUserLikeResponse> getProductsWithUserLike(String search, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.PRODUCT_SORT_FIELDS);
        var userId = User.getCurrentUserId();
        var products = productRepository.fuzzySearchWithUserLike(search, categoryId, minPrice, maxPrice, 0.2, userId, pageable);
        return products.map(productMapper::toDtoFromFlatDto);
    }

    public ProductWithLikesResponse getProductById(Long id) {
        var product = productRepository.findByIdWithLikes(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.toDtoFromFlatDto(product);
    }

    public ProductWithUserLikeResponse getProductWithUserLike(Long id) {
        var product = productRepository.findByIdWithUserLike(id, User.getCurrentUserId())
                .orElseThrow(ProductNotFoundException::new);

        return productMapper.toDtoFromFlatDto(product);
    }

    public ProductDto createProduct(AdminCreateProductRequest request) throws IOException {
        return createProductInternal(request, request.getOwnerId(), true);
    }

    public ProductDto createProduct(CreateProductRequest request, Long sellerId) throws IOException {
        var seller = sellerRepository.findById(sellerId).orElseThrow(SellerNotFoundException::new);
        return createProductInternal(request, seller.getUser().getId(), false);
    }

    private ProductDto createProductInternal(CreateProductDto request,
                                             Long ownerId,
                                             boolean isAdmin
    ) throws IOException {
        var category = categoryRepository.findById(request.getCategoryId()).
                orElseThrow(CategoryNotFoundInBodyException::new);

        var owner = userRepository.findById(ownerId).
                orElseThrow(UserNotFoundException::new);

        if (isAdmin && owner.getRole() == Role.BUYER) {
            throw new ForbiddenOwnerRoleException();
        }

        var imageFileUrl = uploadService.uploadImage(request.getPreviewImage());
        var modelFileUrl = uploadService.uploadModel(request.getFile());

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

    public ProductDto updateProduct(Long id, UpdateProductRequest request) throws IOException {
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

    public ProductDto patchProduct(Long id, @Valid PatchProductRequest request) throws IOException {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).
                    orElseThrow(CategoryNotFoundInBodyException::new);
        }

        var product = productRepository.findById(id).
                orElseThrow(ProductNotFoundException::new);

        String imageFileUrl = null;
        if (request.getPreviewImage() != null) {
            uploadService.deleteFile(product.getPreviewImage());
            imageFileUrl = uploadService.uploadImage(request.getPreviewImage());
        }

        String modelFileUrl = null;
        if (request.getFile() != null) {
            uploadService.deleteFile(product.getFile());
            modelFileUrl = uploadService.uploadModel(request.getFile());
        }

        productMapper.patch(request, product);
        if (category != null) {
            product.setCategory(category);
        }
        if (imageFileUrl != null) {
            product.setPreviewImage(imageFileUrl);
        }
        if (modelFileUrl != null) {
            product.setFile(modelFileUrl);
        }
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public Resource getModelResource(Long productId) throws MalformedURLException {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        String relativePath = product.getFile().startsWith("/")
                ? product.getFile().substring(1)
                : product.getFile();

        Path filePath = Paths.get("uploads").resolve(relativePath).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new ModelResourceNotFoundException();
        }

        return resource;
    }

    public Long getOwnerId(Long productId) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return product.getOwner().getId();
    }

    public Page<ProductWithLikesResponse> getProductsBySellerId(Long id, Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.PRODUCT_SORT_FIELDS);
        var seller = sellerRepository.findById(id).orElseThrow(SellerNotFoundException::new);
        var products = productRepository.findAllByOwnerIdWithLikes(seller.getUser().getId(), pageable);
        return products.map(productMapper::toDtoFromFlatDto);
    }
}
