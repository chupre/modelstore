package com.byllameister.modelstore.payments;

import com.byllameister.modelstore.carts.CartEmptyException;
import com.byllameister.modelstore.carts.CartNotFoundException;
import com.byllameister.modelstore.carts.CartRepository;
import com.byllameister.modelstore.carts.CartService;
import com.byllameister.modelstore.orders.Order;
import com.byllameister.modelstore.orders.OrderItem;
import com.byllameister.modelstore.orders.OrderRepository;
import com.byllameister.modelstore.sellers.Seller;
import com.byllameister.modelstore.sellers.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final CartService cartService;
    private final SellerRepository sellerRepository;
    private final PayoutRepository payoutRepository;

    @Value("${payout.commission}")
    private String commission;

    public CheckoutResponse checkout(CheckoutRequest request) throws PaymentResponseParseFailed {
        var cart = cartRepository.findById(request.getCartId())
                .orElseThrow(CartNotFoundException::new);

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart);

        try {
            var paymentResponse = paymentService.createPayment(
                    order.getTotalPrice(),
                    request.getRedirectUrl());
            cartService.deleteSelectedItems(cart.getId());
            order.setPaymentId(UUID.fromString(paymentResponse.getId()));
            orderRepository.save(order);
            return new CheckoutResponse(order.getId(), paymentResponse.getConfirmationUrl());
        } catch (HttpClientErrorException e) {
            orderRepository.delete(order);
            throw e;
        }
    }

    public void payout(Order order) {
        Map<Seller, BigDecimal> amounts = new HashMap<>();
        for (OrderItem item : order.getOrderItems()) {
            Seller seller = sellerRepository.findByUserId(item.getProduct().getOwner().getId());
            BigDecimal amount = item.getPrice().multiply(BigDecimal.valueOf(1.0 - Double.parseDouble(commission)));
            amounts.merge(seller, amount, BigDecimal::add);
        }

        for (var entry : amounts.entrySet()) {
            Seller seller = entry.getKey();
            BigDecimal amount = entry.getValue();

            var payout = new Payout();
            payout.setSeller(seller);
            payout.setOrder(order);
            payout.setAmount(amount);
            payout.setStatus(PayoutStatus.PENDING);
            payout.setCreatedAt(Instant.now());

            try {
                var payoutResponse = paymentService.createPayout(
                        amount,
                        paymentService.buildPayoutDestination(seller)
                );
                payout.setPaymentId(payoutResponse.getId());
                payoutRepository.save(payout);
            } catch (HttpClientErrorException e) {
                payout.setStatus(PayoutStatus.FAILED);
                payoutRepository.save(payout);
                throw e;
            }
        }
    }
}
