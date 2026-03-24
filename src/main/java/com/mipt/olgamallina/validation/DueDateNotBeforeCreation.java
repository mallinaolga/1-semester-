package com.mipt.olgamallina.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DueDateNotBeforeCreationValidator.class)
public @interface DueDateNotBeforeCreation {
    String message() default "dueDate must not be before creation date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}