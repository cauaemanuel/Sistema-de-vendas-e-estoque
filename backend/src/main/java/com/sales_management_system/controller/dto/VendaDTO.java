package com.sales_management_system.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record VendaDTO(String clienteId, LocalDateTime dataVenda, List<ProdutoVendaDTO> produtos) {
}
