package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.admin.carts.CartExposedResponse;
import com.byllameister.modelstore.admin.carts.CreateCartRequest;
import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.products.ProductNotFoundException;
import com.byllameister.modelstore.users.User;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Controller
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID id) {
        var cart = cartService.getCart(id);

        if (accessDenied(cart.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(cart);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<CartExposedResponse> getCartByUserId(@PathVariable Long userId) {
        if (accessDenied(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            @Valid @RequestBody CreateCartRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        if (accessDenied(request.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var cart = cartService.createCart(request);
        var uri = uriComponentsBuilder
                .path("/carts/{id}")
                .buildAndExpand(cart.getId())
                .toUri();

        return ResponseEntity.created(uri).body(cart);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CartItemDto> addItemToCart(
            @PathVariable UUID id,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        if (accessDenied(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var cartItem = cartService.addItem(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @DeleteMapping("/{id}/items/{productId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable UUID id,
            @PathVariable Long productId
    ) {
        if (accessDenied(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        cartService.deleteCartItem(id, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/items")
    public ResponseEntity<Void> deleteAllItems(@PathVariable UUID id) {
        if (accessDenied(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        cartService.deleteAllCartItems(id);
        return ResponseEntity.noContent().build();
    }

    public boolean accessDenied(UUID id) {
        var userId = cartService.getUserId(id);
        return accessDenied(userId);
    }

    public boolean accessDenied(Long userId) {
        return !userId.equals(User.getCurrentUserId()) && !User.isCurrentUserAdmin();
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
