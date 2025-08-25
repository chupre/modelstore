package com.byllameister.modelstore.admin.carts;

import com.byllameister.modelstore.carts.*;
import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/carts")
@Tag(name = "Admin Cart", description = "Admin methods related to carts.")
@AllArgsConstructor
public class AdminCartController {
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Page<CartExposedResponse>> getCarts(Pageable pageable) {
        var carts = cartService.getCarts(pageable);
        return ResponseEntity.ok(carts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartDto> updateCart(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCartRequest request) {
        var cart = cartService.updateCart(id, request);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/items/{productId}")
    public ResponseEntity<CartItemDto> updateItem(
            @PathVariable UUID id,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        var cartItem = cartService.updateItem(id, productId, request);
        return ResponseEntity.ok(cartItem);
    }

    @ExceptionHandler(ItemAlreadyBoughtException.class)
    public ResponseEntity<ErrorDto> handleItemAlreadyBoughtException(ItemAlreadyBoughtException ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(CartItemAlreadyExists.class)
    public ResponseEntity<ErrorDto> handleCartItemAlreadyExists(CartItemAlreadyExists e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
