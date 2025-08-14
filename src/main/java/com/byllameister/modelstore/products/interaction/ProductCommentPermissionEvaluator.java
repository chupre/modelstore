package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCommentPermissionEvaluator {
    private final ProductCommentRepository productCommentRepository;

    public boolean hasAccessToProductComment(Long commentId) {
        var comment = productCommentRepository.findById(commentId).orElseThrow(ProductCommentNotFoundException::new);
        return comment.getUser().getId().equals(User.getCurrentUserId());
    }
}
