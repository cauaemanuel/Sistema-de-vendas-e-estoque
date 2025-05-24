package com.sales_management_system.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteDTO(
        @NotBlank(message = "O nome do cliente é obrigatório.")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
        String nome,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos.")
        String cpf,

        @NotBlank(message = "O telefone é obrigatório.")
        String telefone,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail informado não é válido.")
        String email
) {}