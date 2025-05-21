package com.sales_management_system.controller.dto;


import jakarta.validation.constraints.NotBlank;

public record ProdutoDTO(String nome, String cartegoria, Double preco, Integer quantidadeEmEstoque) {
}
