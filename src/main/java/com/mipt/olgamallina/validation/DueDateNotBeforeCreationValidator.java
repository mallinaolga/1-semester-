package com.mipt.olgamallina.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DueDateNotBeforeCreationValidator
        implements ConstraintValidator<DueDateNotBeforeCreation, TaskDueDateCheck> {

    @Override
    public boolean isValid(TaskDueDateCheck value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate dueDate = value.getDueDate();
        LocalDateTime createdAt = value.getCreatedAt();

        if (dueDate == null || createdAt == null) {
            return true;
        }

        return !dueDate.isBefore(createdAt.toLocalDate());
    }
}