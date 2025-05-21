package com.sales_management_system.controller.dto;

import jakarta.persistence.Column;

public record ClienteDTO( String nome, String cpf, Integer telefone, String email) {
}
