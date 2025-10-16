package com.sales_management_system.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
    }

    @Test
    void testClienteCreationWithConstructor() {
        cliente = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        
        assertEquals("João Silva", cliente.getNome());
        assertEquals("12345678901", cliente.getCpf());
        assertEquals("(11) 98765-4321", cliente.getTelefone());
        assertEquals("joao@email.com", cliente.getEmail());
    }

    @Test
    void testClienteNoArgsConstructor() {
        cliente = new Cliente();
        
        assertNotNull(cliente);
        assertNull(cliente.getId());
        assertNull(cliente.getNome());
    }

    @Test
    void testClienteSetters() {
        cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome("Maria Santos");
        cliente.setCpf("98765432109");
        cliente.setTelefone("(21) 99999-8888");
        cliente.setEmail("maria@email.com");

        assertEquals(id, cliente.getId());
        assertEquals("Maria Santos", cliente.getNome());
        assertEquals("98765432109", cliente.getCpf());
        assertEquals("(21) 99999-8888", cliente.getTelefone());
        assertEquals("maria@email.com", cliente.getEmail());
    }

    @Test
    void testClienteWithCompras() {
        cliente = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        cliente.setCompras(new ArrayList<>());
        
        assertNotNull(cliente.getCompras());
        assertTrue(cliente.getCompras().isEmpty());
    }

    @Test
    void testClienteEqualsAndHashCode() {
        Cliente cliente1 = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        cliente1.setId(id);

        Cliente cliente2 = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        cliente2.setId(id);

        assertEquals(cliente1, cliente2);
        assertEquals(cliente1.hashCode(), cliente2.hashCode());
    }

    @Test
    void testClienteToString() {
        cliente = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        
        String result = cliente.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("João Silva"));
    }
}
