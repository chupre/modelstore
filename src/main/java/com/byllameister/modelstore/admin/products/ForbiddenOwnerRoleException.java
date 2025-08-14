package com.byllameister.modelstore.admin.products;

public class ForbiddenOwnerRoleException extends RuntimeException {
    public ForbiddenOwnerRoleException() {
        super("Owner role must be ADMIN or SELLER");
    }
}
