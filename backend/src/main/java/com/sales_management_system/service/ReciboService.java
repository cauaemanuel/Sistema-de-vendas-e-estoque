package com.sales_management_system.service;

import com.sales_management_system.Repository.ProdutoRepository;
import com.sales_management_system.controller.dto.ProdutoReciboDTO;
import com.sales_management_system.controller.dto.ReciboDTO;
import com.sales_management_system.entity.Produto;
import com.sales_management_system.entity.Venda;
import org.springframework.stereotype.Service;

@Service
public class ReciboService {

    private ProdutoRepository produtoRepository;

    public ReciboService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ReciboDTO criarRecibo(Venda venda) {
        return new ReciboDTO(
                venda.getCliente().getNome(),
                String.valueOf(venda.getId()),
                venda.getDataVenda(),
                venda.getValorTotal(),
                venda.getItensVenda().stream()
                        .map(produto -> new ProdutoReciboDTO(
                                String.valueOf(produto.getProduto().getId()),
                                produto.getProduto().getNome(),
                                produto.getPrecoUnitario(),
                                produto.getQuantidade()))
                        .toList()
        );
    }
}
