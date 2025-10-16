package com.sales_management_system.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VendaTest {

    private Venda venda;
    private Cliente cliente;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        cliente = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        cliente.setId(UUID.randomUUID());
        
        venda = new Venda();
    }

    @Test
    void testVendaCreation() {
        LocalDateTime dataVenda = LocalDateTime.now();
        venda.setId(id);
        venda.setCliente(cliente);
        venda.setDataVenda(dataVenda);
        venda.setValorTotal(500.0);

        assertEquals(id, venda.getId());
        assertEquals(cliente, venda.getCliente());
        assertEquals(dataVenda, venda.getDataVenda());
        assertEquals(500.0, venda.getValorTotal());
    }

    @Test
    void testVendaWithItensVenda() {
        venda.setItensVenda(new ArrayList<>());
        
        assertNotNull(venda.getItensVenda());
        assertTrue(venda.getItensVenda().isEmpty());
    }

    @Test
    void testVendaEqualsAndHashCode() {
        Venda venda1 = new Venda();
        venda1.setId(id);
        venda1.setValorTotal(500.0);

        Venda venda2 = new Venda();
        venda2.setId(id);
        venda2.setValorTotal(500.0);

        assertEquals(venda1, venda2);
        assertEquals(venda1.hashCode(), venda2.hashCode());
    }

    @Test
    void testVendaToString() {
        venda.setValorTotal(500.0);
        venda.setDataVenda(LocalDateTime.now());
        
        String result = venda.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("500.0"));
    }
}
