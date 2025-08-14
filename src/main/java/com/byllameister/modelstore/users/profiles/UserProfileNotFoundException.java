package com.byllameister.modelstore.users.profiles;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException() {
        super("User profile not found");
    }
}
