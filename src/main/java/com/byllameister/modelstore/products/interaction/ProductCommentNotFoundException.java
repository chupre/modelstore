package com.byllameister.modelstore.products.interaction;

public class ProductCommentNotFoundException extends RuntimeException {
    public ProductCommentNotFoundException() {
        super("Product comment not found");
    }
}
