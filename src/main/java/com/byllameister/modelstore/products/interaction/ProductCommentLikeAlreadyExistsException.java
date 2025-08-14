package com.byllameister.modelstore.products.interaction;

public class ProductCommentLikeAlreadyExistsException extends RuntimeException {
    public ProductCommentLikeAlreadyExistsException() {
        super("Product comment like already exists");
    }
}
