package com.byllameister.modelstore.admin.products;

import com.byllameister.modelstore.categories.CategoryNotFoundInBodyException;
import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.products.*;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/admin/products")
@AllArgsConstructor
public class AdminProductsController {
    ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @ModelAttribute AdminCreateProductRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) throws IOException {
        var product = productService.createProduct(request);
        var uri = uriComponentsBuilder.path("/products/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(uri).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) throws IOException {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateProductRequest request
    ) throws IOException {
        var product = productService.updateProduct(id, request);
        return ResponseEntity.ok().body(product);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute PatchProductRequest request
    ) throws IOException {
        var product = productService.patchProduct(id, request);
        return ResponseEntity.ok().body(product);
    }

    @ExceptionHandler(ModelResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleModelResourceNotFoundException( ModelResourceNotFoundException ex ) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<ErrorDto> handleMalformedURLException() {
        return ResponseEntity.badRequest().body(new ErrorDto("Malformed URL"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorDto> handleMaxUploadSizeExceededException() {
        return ResponseEntity.badRequest().body(new ErrorDto("Max upload size exceeded"));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDto> handleIOException() {
        return ResponseEntity.badRequest().body(new ErrorDto("Unable to upload file"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Void> handleProductNotFound() {
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

    @ExceptionHandler(ForbiddenOwnerRoleException.class)
    public ResponseEntity<ErrorDto> handleForbiddenOwnerRole(ForbiddenOwnerRoleException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}



