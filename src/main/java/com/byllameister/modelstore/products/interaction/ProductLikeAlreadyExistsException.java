package com.byllameister.modelstore.products.interaction;

public class ProductLikeAlreadyExistsException extends RuntimeException {
    public ProductLikeAlreadyExistsException() {
        super("Product like already exists");
    }
}
