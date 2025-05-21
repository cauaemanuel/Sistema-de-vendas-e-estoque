package com.sales_management_system.controller.dto;

import jakarta.persistence.Column;

public record UserDTO( String nome, String email , String password) {
}
