package com.sales_management_system.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemVendaTest {

    private ItemVenda itemVenda;
    private Produto produto;
    private Venda venda;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        
        produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Produto Teste");
        produto.setPreco(100.0);
        
        venda = new Venda();
        venda.setId(UUID.randomUUID());
        
        itemVenda = new ItemVenda();
    }

    @Test
    void testItemVendaCreation() {
        itemVenda.setId(id);
        itemVenda.setProduto(produto);
        itemVenda.setVenda(venda);
        itemVenda.setQuantidade(5);
        itemVenda.setPrecoUnitario(100.0);

        assertEquals(id, itemVenda.getId());
        assertEquals(produto, itemVenda.getProduto());
        assertEquals(venda, itemVenda.getVenda());
        assertEquals(5, itemVenda.getQuantidade());
        assertEquals(100.0, itemVenda.getPrecoUnitario());
    }

    @Test
    void testItemVendaTotalCalculation() {
        itemVenda.setQuantidade(3);
        itemVenda.setPrecoUnitario(50.0);
        
        double total = itemVenda.getQuantidade() * itemVenda.getPrecoUnitario();
        
        assertEquals(150.0, total);
    }

    @Test
    void testItemVendaEqualsAndHashCode() {
        ItemVenda itemVenda1 = new ItemVenda();
        itemVenda1.setId(id);
        itemVenda1.setQuantidade(5);

        ItemVenda itemVenda2 = new ItemVenda();
        itemVenda2.setId(id);
        itemVenda2.setQuantidade(5);

        assertEquals(itemVenda1, itemVenda2);
        assertEquals(itemVenda1.hashCode(), itemVenda2.hashCode());
    }

    @Test
    void testItemVendaToString() {
        itemVenda.setQuantidade(5);
        itemVenda.setPrecoUnitario(100.0);
        
        String result = itemVenda.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("5"));
        assertTrue(result.contains("100.0"));
    }
}
