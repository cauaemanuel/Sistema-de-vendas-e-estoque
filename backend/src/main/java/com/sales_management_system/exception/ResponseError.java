package com.sales_management_system.exception;

import org.springframework.http.HttpStatus;

public record ResponseError(HttpStatus httpStatus, String message) {
}