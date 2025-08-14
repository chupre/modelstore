package com.byllameister.modelstore.carts;

public class ItemAlreadyBoughtException extends RuntimeException {
    public ItemAlreadyBoughtException() {
        super("One or more items have already been bought");
    }
}
