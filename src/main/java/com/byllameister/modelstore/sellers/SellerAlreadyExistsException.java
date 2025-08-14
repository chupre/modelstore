package com.byllameister.modelstore.sellers;

public class SellerAlreadyExistsException extends RuntimeException {
    public SellerAlreadyExistsException() {
        super("Seller already exists");
    }
}
