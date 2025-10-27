package com.mipt.hm8;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
    String pattern() default "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    String message() default "must be a valid email";
}
