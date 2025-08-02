package com.byllameister.modelstore.carts;

public class CartAlreadyExistException extends RuntimeException {
    public CartAlreadyExistException() {
        super("Cart already exists");
    }
}
