package com.sales_management_system.service;

import com.sales_management_system.Repository.ProdutoRepository;
import com.sales_management_system.controller.dto.ProdutoDTO;
import com.sales_management_system.entity.Produto;
import com.sales_management_system.mapper.ProdutoMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMap produtoMap;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoDTO produtoDTO;
    private UUID produtoId;

    @BeforeEach
    void setUp() {
        produtoId = UUID.randomUUID();
        
        produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Produto Teste");
        produto.setCartegoria("Categoria Teste");
        produto.setPreco(100.0);
        produto.setQuantidadeEmEstoque(10);

        produtoDTO = new ProdutoDTO("Produto Teste", "Categoria Teste", 100.0, 10);
    }

    @Test
    void testCreate_ShouldCreateProduto() {
        when(produtoMap.fromDTO(produtoDTO)).thenReturn(produto);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto result = produtoService.create(produtoDTO);

        assertNotNull(result);
        assertEquals("Produto Teste", result.getNome());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testFindAll_ShouldReturnListOfProdutos() {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(produto);

        when(produtoRepository.findAll()).thenReturn(produtos);
        when(produtoMap.fromProduto(any(Produto.class))).thenReturn(produtoDTO);

        List<ProdutoDTO> result = produtoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ShouldThrowExceptionWhenEmpty() {
        when(produtoRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> produtoService.findAll());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ShouldReturnProduto() {
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoMap.fromProduto(produto)).thenReturn(produtoDTO);

        ProdutoDTO result = produtoService.findById(produtoId.toString());

        assertNotNull(result);
        assertEquals("Produto Teste", result.nome());
        verify(produtoRepository, times(1)).findById(produtoId);
    }

    @Test
    void testFindById_ShouldThrowExceptionWhenNotFound() {
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> produtoService.findById(produtoId.toString()));
        verify(produtoRepository, times(1)).findById(produtoId);
    }

    @Test
    void testDeleteById_ShouldDeleteProduto() {
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).deleteById(produtoId);

        produtoService.deleteById(produtoId.toString());

        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(1)).deleteById(produtoId);
    }

    @Test
    void testUpdateProduto_ShouldUpdateAndReturnProduto() {
        ProdutoDTO updatedDTO = new ProdutoDTO("Produto Atualizado", "Nova Categoria", 150.0, 20);
        Produto updatedProduto = new Produto();
        updatedProduto.setId(produtoId);
        updatedProduto.setNome("Produto Atualizado");
        updatedProduto.setCartegoria("Nova Categoria");
        updatedProduto.setPreco(150.0);
        updatedProduto.setQuantidadeEmEstoque(20);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoMap.updateFromDTO(updatedDTO, produto)).thenReturn(updatedProduto);
        when(produtoRepository.save(updatedProduto)).thenReturn(updatedProduto);
        when(produtoMap.fromProduto(updatedProduto)).thenReturn(updatedDTO);

        ProdutoDTO result = produtoService.updateProduto(updatedDTO, produtoId.toString());

        assertNotNull(result);
        assertEquals("Produto Atualizado", result.nome());
        assertEquals(150.0, result.preco());
        verify(produtoRepository, times(1)).save(updatedProduto);
    }

    @Test
    void testFindEntityById_ShouldReturnProduto() {
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        Produto result = produtoService.findEntityByid(produtoId.toString());

        assertNotNull(result);
        assertEquals(produtoId, result.getId());
        verify(produtoRepository, times(1)).findById(produtoId);
    }

    @Test
    void testSave_ShouldSaveProduto() {
        when(produtoRepository.save(produto)).thenReturn(produto);

        produtoService.save(produto);

        verify(produtoRepository, times(1)).save(produto);
    }
}
