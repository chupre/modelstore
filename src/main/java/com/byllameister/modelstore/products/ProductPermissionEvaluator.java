package com.byllameister.modelstore.products;

import com.byllameister.modelstore.orders.OrderRepository;
import com.byllameister.modelstore.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductPermissionEvaluator {
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public boolean hasAccess(Long productId) {
        Long ownerId = productService.getOwnerId(productId);
        Long currentUserId = User.getCurrentUserId();
        boolean hasBought = orderRepository.hasUserBoughtProduct(currentUserId, productId);

        return ownerId.equals(User.getCurrentUserId()) ||
                User.isCurrentUserAdmin() ||
                hasBought;
    }
}