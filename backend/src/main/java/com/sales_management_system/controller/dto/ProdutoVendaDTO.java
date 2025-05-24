package com.sales_management_system.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoVendaDTO(
        @NotBlank(message = "O ID do produto é obrigatório.")
        String produtoId,

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
        Integer quantidade
) {}