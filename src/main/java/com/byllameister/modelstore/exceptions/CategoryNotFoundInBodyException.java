package com.byllameister.modelstore.exceptions;

public class CategoryNotFoundInBodyException extends RuntimeException {
    public CategoryNotFoundInBodyException() {
        super("Category not found");
    }
}
