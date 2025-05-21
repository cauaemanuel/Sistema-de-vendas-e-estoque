package com.sales_management_system.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoDTO(
        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(min = 2, max = 100, message = "O nome do produto deve ter entre 2 e 100 caracteres.")
        String nome,

        @NotBlank(message = "A categoria é obrigatória.")
        String cartegoria,

        @NotNull(message = "O preço é obrigatório.")
        @Min(value = 0, message = "O preço não pode ser negativo.")
        Double preco,

        @NotNull(message = "A quantidade em estoque é obrigatória.")
        @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
        Integer quantidadeEmEstoque
) {}