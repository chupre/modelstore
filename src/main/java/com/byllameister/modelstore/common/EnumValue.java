package com.byllameister.modelstore.common;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {
    String message() default "must be any of {enumClass}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass();
    boolean ignoreCase() default false;
}