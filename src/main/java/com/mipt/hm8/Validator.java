package com.mipt.hm8;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_RE =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static ValidationResult validate(Object object) {
        ValidationResult res = ValidationResult.ok();

        if (object == null) {
            res.addError("<root>", "object is null");
            return res;
        }

        Class<?> clazz = object.getClass();

        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            String path = f.getName();
            Object value;
            try {
                value = f.get(object);
            } catch (IllegalAccessException e) {
                continue;
            }

            // @NotNull
            NotNull anNotNull = f.getAnnotation(NotNull.class);
            if (anNotNull != null && value == null) {
                res.addError(path, anNotNull.message());
                continue;
            }

            // @Size — по условию для строк
            Size anSize = f.getAnnotation(Size.class);
            if (anSize != null && value != null) {
                if (value instanceof CharSequence s) {
                    int len = s.length();
                    if (len < anSize.min() || len > anSize.max()) {
                        res.addError(path, anSize.message());
                    }
                } else {
                    res.addError(path, "Size applicable only to String");
                }
            }

            Range anRange = f.getAnnotation(Range.class);
            if (anRange != null && value != null) {
                Long num = toLong(value);
                if (num == null) {
                    res.addError(path, "Range applicable only to numeric types");
                } else if (num < anRange.min() || num > anRange.max()) {
                    res.addError(path, anRange.message());
                }
            }
            Email anEmail = f.getAnnotation(Email.class);
            if (anEmail != null && value != null) {
                if (!(value instanceof CharSequence s) || !EMAIL_RE.matcher(s).matches()) {
                    res.addError(path, anEmail.message());
                }
            }
        }

        return res;
    }

    private static Long toLong(Object v) {
        if (v instanceof Byte b) return (long) b;
        if (v instanceof Short s) return (long) s;
        if (v instanceof Integer i) return (long) i;
        if (v instanceof Long l) return l;
        if (v instanceof Float f) return (long) f.floatValue();
        if (v instanceof Double d) return (long) d.doubleValue();
        return null;
    }
}
