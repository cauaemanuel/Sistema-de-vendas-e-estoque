package com.sales_management_system.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> responseStatusException(ResponseStatusException ex, WebRequest request) {
        var responseError = new ResponseError((HttpStatus) ex.getStatusCode(), ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(responseError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = String.join(", ", errors);
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    // Tratamento para argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    // Tratamento para validação de parâmetros 
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseError> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining(", "));

        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }
}
