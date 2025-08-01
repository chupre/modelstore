package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.users.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDto>> getCarts(Pageable pageable) {
        var carts = cartService.getCarts(pageable);
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID id) {
        var cart = cartService.getCart(id);

        if (!cart.getUserId().equals(User.getCurrentUserId())
            && !User.isCurrentUserAdmin()
        ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(cart);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.notFound().build();
    }
}
