package com.byllameister.modelstore.products;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotBlankIfNotNullValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIfNotNull {
    String message() default "Field cannot be blank when provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}