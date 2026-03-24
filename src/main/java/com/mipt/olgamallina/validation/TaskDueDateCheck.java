package com.mipt.olgamallina.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TaskDueDateCheck {
    LocalDate getDueDate();
    LocalDateTime getCreatedAt();
}