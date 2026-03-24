package com.mipt.olgamallina.handler;

import com.mipt.olgamallina.dto.ErrorResponse;
import com.mipt.olgamallina.exception.AttachmentNotFoundException;
import com.mipt.olgamallina.exception.FileStorageException;
import com.mipt.olgamallina.exception.TaskNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", request, details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        Map<String, Object> details = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            details.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return buildResponse(HttpStatus.BAD_REQUEST, "Constraint violation", request, details);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", request, null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleNoHandler(
            NoHandlerFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "No handler found", request, null);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleTaskNotFound(
            TaskNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(AttachmentNotFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleAttachmentNotFound(
            AttachmentNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(FileStorageException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleFileStorage(
            FileStorageException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleOther(
            Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error", request, null);
    }

    private org.springframework.http.ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, Object> details) {

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                details != null ? details : new HashMap<>()
        );

        return org.springframework.http.ResponseEntity.status(status).body(error);
    }
}