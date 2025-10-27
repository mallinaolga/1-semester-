package com.mipt.hm8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {
    private boolean isValid = true;
    private final List<String> errors = new ArrayList<>();

    public static ValidationResult ok() { return new ValidationResult(); }
    public static ValidationResult fail(String error) {
        ValidationResult r = new ValidationResult();
        r.addError(error);
        return r;
    }

    public boolean isValid() { return isValid; }
    public List<String> getErrors() { return Collections.unmodifiableList(errors); }

    public void addError(String message) {
        if (message == null || message.isBlank()) return;
        errors.add(message);
        isValid = false;
    }
    public void addError(String path, String message) { addError(path + ": " + message); }

    public void merge(ValidationResult other) {
        if (other == null) return;
        for (String e : other.errors) addError(e);
    }

    public boolean hasErrors() { return !errors.isEmpty(); }

    @Override
    public String toString() {
        return isValid ? "ValidationResult{valid}" : "ValidationResult{errors=" + errors + "}";
    }
}
