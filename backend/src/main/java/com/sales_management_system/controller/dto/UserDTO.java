package com.sales_management_system.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank(message = "O nome do usuário é obrigatório.")
        @Size(min = 2, max = 100, message = "O nome do usuário deve ter entre 2 e 100 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail informado não é válido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
        String password
) {}