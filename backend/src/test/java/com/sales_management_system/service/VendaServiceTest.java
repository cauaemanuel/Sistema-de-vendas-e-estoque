package com.sales_management_system.service;

import com.sales_management_system.Repository.ItemVendaRepository;
import com.sales_management_system.Repository.VendaRepository;
import com.sales_management_system.controller.dto.ProdutoVendaDTO;
import com.sales_management_system.controller.dto.ReciboDTO;
import com.sales_management_system.controller.dto.VendaDTO;
import com.sales_management_system.entity.Cliente;
import com.sales_management_system.entity.Produto;
import com.sales_management_system.entity.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ItemVendaRepository itemVendaRepository;

    @Mock
    private ReciboService reciboService;

    @InjectMocks
    private VendaService vendaService;

    private Cliente cliente;
    private Produto produto;
    private Venda venda;
    private VendaDTO vendaDTO;
    private UUID clienteId;
    private UUID produtoId;
    private UUID vendaId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        produtoId = UUID.randomUUID();
        vendaId = UUID.randomUUID();

        cliente = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        cliente.setId(clienteId);

        produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Produto Teste");
        produto.setCartegoria("Categoria Teste");
        produto.setPreco(100.0);
        produto.setQuantidadeEmEstoque(10);

        venda = new Venda();
        venda.setId(vendaId);
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDateTime.now());
        venda.setValorTotal(200.0);

        ProdutoVendaDTO produtoVendaDTO = new ProdutoVendaDTO(produtoId.toString(), 2);
        List<ProdutoVendaDTO> produtos = new ArrayList<>();
        produtos.add(produtoVendaDTO);
        
        vendaDTO = new VendaDTO(clienteId.toString(), LocalDateTime.now(), produtos);
    }

    @Test
    void testRealizarVenda_ShouldCreateVendaSuccessfully() {
        ReciboDTO expectedRecibo = new ReciboDTO(
            cliente.getNome(),
            vendaId.toString(),
            venda.getDataVenda(),
            venda.getValorTotal(),
            new ArrayList<>()
        );

        when(clienteService.findEntityByid(clienteId.toString())).thenReturn(cliente);
        when(produtoService.findEntityByid(produtoId.toString())).thenReturn(produto);
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
        when(itemVendaRepository.saveAll(any())).thenReturn(new ArrayList<>());
        when(reciboService.criarRecibo(any(Venda.class))).thenReturn(expectedRecibo);
        doNothing().when(produtoService).save(any(Produto.class));

        ReciboDTO result = vendaService.realizarVenda(vendaDTO);

        assertNotNull(result);
        assertEquals(vendaId.toString(), result.idVenda());
        verify(vendaRepository, times(1)).save(any(Venda.class));
        verify(produtoService, times(1)).save(any(Produto.class));
        verify(itemVendaRepository, times(1)).saveAll(any());
    }

    @Test
    void testRealizarVenda_ShouldThrowExceptionWhenInsufficientStock() {
        produto.setQuantidadeEmEstoque(1);
        ProdutoVendaDTO produtoVendaDTO = new ProdutoVendaDTO(produtoId.toString(), 5);
        List<ProdutoVendaDTO> produtos = new ArrayList<>();
        produtos.add(produtoVendaDTO);
        VendaDTO vendaDTOWithExcessiveQuantity = new VendaDTO(clienteId.toString(), LocalDateTime.now(), produtos);

        when(clienteService.findEntityByid(clienteId.toString())).thenReturn(cliente);
        when(produtoService.findEntityByid(produtoId.toString())).thenReturn(produto);

        assertThrows(ResponseStatusException.class, () -> vendaService.realizarVenda(vendaDTOWithExcessiveQuantity));
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    void testBuscarReciboPorIdVenda_ShouldReturnRecibo() {
        ReciboDTO expectedRecibo = new ReciboDTO(
            cliente.getNome(),
            vendaId.toString(),
            venda.getDataVenda(),
            venda.getValorTotal(),
            new ArrayList<>()
        );

        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(venda));
        when(reciboService.criarRecibo(venda)).thenReturn(expectedRecibo);

        ReciboDTO result = vendaService.buscarReciboPorIdVenda(vendaId.toString());

        assertNotNull(result);
        assertEquals(vendaId.toString(), result.idVenda());
        verify(vendaRepository, times(1)).findById(vendaId);
        verify(reciboService, times(1)).criarRecibo(venda);
    }

    @Test
    void testBuscarReciboPorIdVenda_ShouldThrowExceptionWhenVendaNotFound() {
        when(vendaRepository.findById(vendaId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> vendaService.buscarReciboPorIdVenda(vendaId.toString()));
        verify(vendaRepository, times(1)).findById(vendaId);
        verify(reciboService, never()).criarRecibo(any());
    }
}
