package com.byllameister.modelstore.payments;

import com.byllameister.modelstore.carts.CartEmptyException;
import com.byllameister.modelstore.carts.CartNotFoundException;
import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.orders.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @PreAuthorize("@cartPermissionEvaluator.hasAccess(#request.cartId)")
    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest request) throws PaymentResponseParseFailed {
        var checkout = checkoutService.checkout(request);
        return ResponseEntity.ok(checkout);
    }

    @PostMapping("/a")
    public ResponseEntity<Void> payout(@RequestParam(name = "id") Long id)
    {
        var order = orderRepository.getOrderById(id);
        checkoutService.payout(order);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(PaymentResponseParseFailed.class)
    public ResponseEntity<ErrorDto> handlePaymentResponseParseFailed(PaymentResponseParseFailed e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(CartNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ErrorDto> handleException(CartEmptyException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorDto> handleException(HttpClientErrorException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }
}
