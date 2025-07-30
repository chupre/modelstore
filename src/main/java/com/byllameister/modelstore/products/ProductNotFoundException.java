package com.byllameister.modelstore.products;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("Product not found");
    }
}
