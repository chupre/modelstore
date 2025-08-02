package com.byllameister.modelstore.carts;


public class CartItemAlreadyExists extends RuntimeException {
    public CartItemAlreadyExists() {
        super("Cart item already exists");
    }
}
