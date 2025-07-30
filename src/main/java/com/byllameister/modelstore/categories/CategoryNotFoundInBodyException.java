package com.byllameister.modelstore.categories;

public class CategoryNotFoundInBodyException extends RuntimeException {
    public CategoryNotFoundInBodyException() {
        super("Category not found");
    }
}
