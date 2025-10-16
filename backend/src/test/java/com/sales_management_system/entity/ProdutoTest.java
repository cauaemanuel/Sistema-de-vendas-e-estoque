package com.sales_management_system.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    private Produto produto;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        produto = new Produto();
    }

    @Test
    void testProdutoCreation() {
        produto.setId(id);
        produto.setNome("Produto Teste");
        produto.setCartegoria("Categoria Teste");
        produto.setPreco(100.0);
        produto.setQuantidadeEmEstoque(10);

        assertEquals(id, produto.getId());
        assertEquals("Produto Teste", produto.getNome());
        assertEquals("Categoria Teste", produto.getCartegoria());
        assertEquals(100.0, produto.getPreco());
        assertEquals(10, produto.getQuantidadeEmEstoque());
    }

    @Test
    void testProdutoWithItensVenda() {
        produto.setItensVenda(new ArrayList<>());
        
        assertNotNull(produto.getItensVenda());
        assertTrue(produto.getItensVenda().isEmpty());
    }

    @Test
    void testProdutoEqualsAndHashCode() {
        Produto produto1 = new Produto();
        produto1.setId(id);
        produto1.setNome("Produto 1");

        Produto produto2 = new Produto();
        produto2.setId(id);
        produto2.setNome("Produto 1");

        assertEquals(produto1, produto2);
        assertEquals(produto1.hashCode(), produto2.hashCode());
    }

    @Test
    void testProdutoToString() {
        produto.setNome("Produto Teste");
        produto.setCartegoria("Categoria Teste");
        produto.setPreco(100.0);
        
        String result = produto.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("Produto Teste"));
    }
}
