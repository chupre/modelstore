package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.products.ProductNotFoundException;
import com.byllameister.modelstore.products.ProductRepository;
import com.byllameister.modelstore.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerPermissionEvaluator {
    private final ProductRepository productRepository;

    public boolean hasAccessToProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return product.getOwner().getId().equals(User.getCurrentUserId());
    }
}