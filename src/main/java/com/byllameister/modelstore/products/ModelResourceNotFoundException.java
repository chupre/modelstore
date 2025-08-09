package com.byllameister.modelstore.products;

public class ModelResourceNotFoundException extends RuntimeException {
    public ModelResourceNotFoundException() {
        super("Model resource not found");
    }
}
