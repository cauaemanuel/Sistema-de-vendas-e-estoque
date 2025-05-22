package com.sales_management_system.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ReciboDTO(String nome, String idVenda, LocalDateTime dataDaVenda, Double ValorTotal, List<ProdutoReciboDTO> produtos) {
}
