package com.sales_management_system.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record VendaDTO(
        @NotBlank(message = "O ID do cliente é obrigatório.")
        String clienteId,

        @NotNull(message = "A data da venda é obrigatória.")
        LocalDateTime dataVenda,

        @NotNull(message = "A lista de produtos não pode ser nula.")
        @Size(min = 1, message = "A venda deve conter pelo menos um produto.")
        List<ProdutoVendaDTO> produtos
) {}