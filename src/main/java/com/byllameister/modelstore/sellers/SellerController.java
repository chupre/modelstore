package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.products.*;
import com.byllameister.modelstore.products.ProductWithLikesResponse;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/sellers")
@AllArgsConstructor
public class SellerController {
    private final SellerService sellerService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<SellerResponse> becomeSeller(
            @Valid @RequestBody CreateSellerRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var seller = sellerService.becomeSeller(request);
        var uri = uriComponentsBuilder.path("/sellers/{productId}").buildAndExpand(seller.getId()).toUri();
        return ResponseEntity.created(uri).body(seller);
    }

    @GetMapping("/me")
    public ResponseEntity<SellerWithStatsResponse> getCurrentSeller() {
        var seller = sellerService.getCurrentSeller();
        return ResponseEntity.ok(seller);
    }

    @PutMapping("/me")
    public ResponseEntity<SellerResponse> updateSeller(
            @Valid @RequestBody UpdateSellerRequest request
    ) {
        var id = sellerService.getCurrentSeller().getId();
        var seller = sellerService.updateSeller(id, request);
        return ResponseEntity.ok(seller);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteSeller() {
        var id = sellerService.getCurrentSeller().getId();
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/products")
    public Page<ProductWithLikesResponse> getSellerProducts(Pageable pageable) {
        var id = sellerService.getCurrentSeller().getId();
        return productService.getProductsBySellerId(id, pageable);
    }

    @PostMapping("/me/products")
    public ResponseEntity<ProductDto> createProduct(
            @Valid @ModelAttribute CreateProductRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) throws IOException {
        var id = sellerService.getCurrentSeller().getId();
        var product = productService.createProduct(request, id);

        var uri = uriComponentsBuilder.path("/sellers/{productId}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping("/me/products/{id}")
    @PreAuthorize("@sellerPermissionEvaluator.hasAccessToProduct(#id)")
    public ResponseEntity<ProductDto> updateProduct(
            @Valid @ModelAttribute UpdateProductRequest request,
            @PathVariable Long id
    ) throws IOException {
        var product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/me/products/{id}")
    @PreAuthorize("@sellerPermissionEvaluator.hasAccessToProduct(#id)")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute PatchProductRequest request
    ) throws IOException {
        var product = productService.patchProduct(id, request);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/me/products/{id}")
    @PreAuthorize("@sellerPermissionEvaluator.hasAccessToProduct(#id)")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) throws IOException {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<ErrorDto> handleSellerNotFoundException(SellerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(SellerAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleSellerAlreadyExistsException(SellerAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDto> handleIOException() {
        return ResponseEntity.badRequest().body(new ErrorDto("Unable to upload file"));
    }
}
