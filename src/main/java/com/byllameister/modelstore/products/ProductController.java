package com.byllameister.modelstore.products;

import com.byllameister.modelstore.categories.CategoryNotFoundInBodyException;
import com.byllameister.modelstore.categories.CategoryNotFoundInQueryException;
import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.products.interaction.ProductWithLikesDto;
import com.byllameister.modelstore.users.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;


@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Page<ProductWithLikesDto> getAllProducts(
            @RequestParam(name = "search", required = false ) String search,
            @RequestParam(name = "categoryId", required = false ) Long categoryId,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            Pageable pageable
    ) {
        return productService.getProducts(search, categoryId, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    public ProductWithLikesDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PreAuthorize("@productPermissionEvaluator.hasAccess(#id)")
    @GetMapping("/{id}/downloadModel")
    public ResponseEntity<Resource> downloadModel(@PathVariable Long id) throws MalformedURLException {
        var resource = productService.getModelResource(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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
