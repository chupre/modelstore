package com.byllameister.modelstore.users;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException() {
        super("Username is already in use");
    }
}
