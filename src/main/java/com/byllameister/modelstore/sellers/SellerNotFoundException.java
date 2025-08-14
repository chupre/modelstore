package com.byllameister.modelstore.sellers;

public class SellerNotFoundException extends RuntimeException {
    public SellerNotFoundException() {
        super("Seller not found");
    }
}
