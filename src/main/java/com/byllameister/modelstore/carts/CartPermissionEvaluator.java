package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.users.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CartPermissionEvaluator {
    private CartService cartService;

    public boolean hasAccess(UUID id) {
        var userId = cartService.getUserId(id);
        return hasAccess(userId);
    }

    public boolean hasAccess(Long userId) {
        return userId.equals(User.getCurrentUserId()) || User.isCurrentUserAdmin();
    }
}
