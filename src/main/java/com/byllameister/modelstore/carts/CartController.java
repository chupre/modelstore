package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.admin.carts.CartExposedResponse;
import com.byllameister.modelstore.admin.carts.CreateCartRequest;
import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.products.ProductNotFoundException;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Controller
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID id) {
        var cart = cartService.getCart(id);

        return ResponseEntity.ok(cart);
    }

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#userId)")
    @GetMapping("/users/{userId}")
    public ResponseEntity<CartExposedResponse> getCartByUserId(@PathVariable Long userId) {
        var cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#request.userId)")
    @PostMapping
    public ResponseEntity<CartDto> createCart(
            @Valid @RequestBody CreateCartRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var cart = cartService.createCart(request);
        var uri = uriComponentsBuilder
                .path("/carts/{id}")
                .buildAndExpand(cart.getId())
                .toUri();

        return ResponseEntity.created(uri).body(cart);
    }

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#id)")
    @PostMapping("/{id}/items")
    public ResponseEntity<CartItemDto> addItemToCart(
            @PathVariable UUID id,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        var cartItem = cartService.addItem(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#id)")
    @DeleteMapping("/{id}/items/{productId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable UUID id,
            @PathVariable Long productId
    ) {
        cartService.deleteCartItem(id, productId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#id)")
    @DeleteMapping("/{id}/items")
    public ResponseEntity<Void> deleteAllItems(
            @PathVariable UUID id,
            @RequestParam(name = "selected", required = false) Boolean selected
    ) {
        if (selected != null) {
            cartService.deleteSelectedItems(id);
        } else {
            cartService.deleteAllCartItems(id);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#id)")
    @PostMapping("/{id}/items/{productId}/toggleSelect")
    public ResponseEntity<CartItemDto> toggleSelectItem(
            @PathVariable UUID id,
            @PathVariable Long productId
    ) {
        var cartItem = cartService.toggleSelectCartItem(id, productId);
        return ResponseEntity.ok(cartItem);
    }

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#id)")
    @PostMapping("/{id}/items/selectAll")
    public ResponseEntity<CartDto> selectAll(@PathVariable UUID id) {
        var cart = cartService.selectAllItems(id);
        return ResponseEntity.ok(cart);
    }

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#id)")
    @PostMapping("/{id}/items/unselectAll")
    public ResponseEntity<CartDto> unselectAll(@PathVariable UUID id) {
        var cart = cartService.unselectAllItems(id);
        return ResponseEntity.ok(cart);
    }

    @ExceptionHandler(ItemAlreadyBoughtException.class)
    public ResponseEntity<ErrorDto> handleItemAlreadyBoughtException(ItemAlreadyBoughtException ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(CartItemAlreadyExists.class)
    public ResponseEntity<ErrorDto> handleCartItemAlreadyExists(CartItemAlreadyExists e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(CartAlreadyExistException.class)
    public ResponseEntity<ErrorDto> handleException(CartAlreadyExistException ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
