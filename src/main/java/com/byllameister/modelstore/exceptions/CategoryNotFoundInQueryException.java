package com.byllameister.modelstore.exceptions;

public class CategoryNotFoundInQueryException extends RuntimeException {
    public CategoryNotFoundInQueryException() {
        super("Category not found: ");
    }
}
