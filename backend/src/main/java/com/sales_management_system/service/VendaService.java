package com.sales_management_system.service;

import com.sales_management_system.Repository.ClienteRepository;
import com.sales_management_system.Repository.ItemVendaRepository;
import com.sales_management_system.Repository.ProdutoRepository;
import com.sales_management_system.Repository.VendaRepository;
import com.sales_management_system.controller.dto.ProdutoReciboDTO;
import com.sales_management_system.controller.dto.ReciboDTO;
import com.sales_management_system.controller.dto.VendaDTO;
import com.sales_management_system.entity.ItemVenda;
import com.sales_management_system.entity.Venda;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VendaService {

    private ClienteRepository clienteRepository;
    private ProdutoRepository produtoRepository;
    private VendaRepository vendaRepository;
    private ItemVendaRepository itemVendaRepository;
    private ReciboService reciboService;

    public VendaService(ClienteRepository clienteRepository,
                        ProdutoRepository produtoRepository,
                        VendaRepository vendaRepository,
                        ItemVendaRepository itemVendaRepository,
                        ReciboService reciboService) {
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.reciboService = reciboService;
    }

    @Transactional
    public ReciboDTO realizarVenda(VendaDTO vendaDTO) {

        var clienteId = UUID.fromString(vendaDTO.clienteId());//procurando o cliente pelo id
        var cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        var venda = new Venda();//criando a venda
        venda.setCliente(cliente);
        venda.setDataVenda(vendaDTO.dataVenda());

        List<ItemVenda> itens = new ArrayList<>();//lista de itens da venda

        var produtos = vendaDTO.produtos();// iniciando logica de produtos e cadastrando venda
        produtos.stream().forEach( p -> {

            var produtoId = UUID.fromString(p.produtoId());//procurando o produto pelo id
            var produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

            if (produto.getQuantidadeEmEstoque() < p.quantidade()) { //verificando se o produto tem estoque suficiente
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto com estoque insuficiente");
            }

            var itemVenda = new ItemVenda();//criando item produto na venda
            itemVenda.setVenda(venda);
            itemVenda.setProduto(produto);
            itemVenda.setQuantidade(p.quantidade());
            itemVenda.setPrecoUnitario(produto.getPreco());

            itens.add(itemVenda);//adicionando o item na lista de itens da venda

            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - p.quantidade());//atualizando o estoque do produto
            produtoRepository.save(produto);//salvando o produto atualizado
        });

        var total = itens.stream()
                .mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade())
                .sum();

        venda.setItensVenda(itens);//adicionando os itens na venda
        venda.setValorTotal(total);//atualizando o valor total da venda

        vendaRepository.save(venda);
        itemVendaRepository.saveAll(itens);//salvando todos os itens da venda

        //cria Recibo
        return reciboService.criarRecibo(venda);

    }

    public ReciboDTO buscarReciboPorIdVenda(String idVenda){
        //lógica para buscar venda
        var vendaId = UUID.fromString(idVenda);
        var venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada"));

        return reciboService.criarRecibo(venda);
    }

}
