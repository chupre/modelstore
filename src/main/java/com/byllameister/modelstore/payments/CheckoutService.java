package com.byllameister.modelstore.payments;

import com.byllameister.modelstore.carts.CartEmptyException;
import com.byllameister.modelstore.carts.CartNotFoundException;
import com.byllameister.modelstore.carts.CartRepository;
import com.byllameister.modelstore.carts.CartService;
import com.byllameister.modelstore.orders.Order;
import com.byllameister.modelstore.orders.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request) throws PaymentResponseParseFailed {
        var cart = cartRepository.findById(request.getCartId())
                .orElseThrow(CartNotFoundException::new);

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart);

        try {
            var paymentResponse = paymentGatewayService.createPayment(
                    order.getTotalPrice(),
                    request.getCurrency(),
                    request.getRedirectUrl());
            cartService.deleteSelectedItems(cart.getId());
            order.setPaymentId(UUID.fromString(paymentResponse.getPaymentId()));
            orderRepository.save(order);
            return new CheckoutResponse(order.getId(), paymentResponse.getConfirmationUrl());
        } catch (HttpClientErrorException e) {
            orderRepository.delete(order);
            throw e;
        }
    }
}
