package com.byllameister.modelstore.products;

import com.byllameister.modelstore.categories.CategoryNotFoundInBodyException;
import com.byllameister.modelstore.categories.CategoryNotFoundInQueryException;
import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Page<ProductDto> getAllProducts(
            @RequestParam(name = "categoryId", required = false ) Long categoryId,
            @RequestParam(name = "search", required = false ) String search,
            Pageable pageable) {
        if (categoryId == null) {
            return productService.getAllProducts(search, pageable);
        } else {
            return productService.getProductsByCategoryId(categoryId, pageable);
        }
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductDto productDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var product = productService.createProduct(productDto);
        var uri = uriComponentsBuilder.path("/products/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(uri).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto productDto
    ) {
        var product = productService.updateProductById(id, productDto);
        return ResponseEntity.ok().body(product);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Void> handleProductNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CategoryNotFoundInQueryException.class)
    public ResponseEntity<Void> handleCategoryNotFoundInQuery() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CategoryNotFoundInBodyException.class)
    public ResponseEntity<ErrorDto> handleCategoryNotFoundInBody(CategoryNotFoundInBodyException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
