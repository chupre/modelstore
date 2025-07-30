package com.byllameister.modelstore.categories;

public class CategoryNotFoundInQueryException extends RuntimeException {
    public CategoryNotFoundInQueryException() {
        super("Category not found: ");
    }
}
