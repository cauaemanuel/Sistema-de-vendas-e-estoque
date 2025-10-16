package com.sales_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_management_system.controller.dto.ProdutoDTO;
import com.sales_management_system.entity.Produto;
import com.sales_management_system.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ProdutoController.class, 
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
    classes = {com.sales_management_system.security.config.SecurityConfig.class, 
               com.sales_management_system.security.config.SecurityFilter.class}))
@WithMockUser
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService produtoService;

    private ProdutoDTO produtoDTO;
    private Produto produto;
    private UUID produtoId;

    @BeforeEach
    void setUp() {
        produtoId = UUID.randomUUID();
        
        produtoDTO = new ProdutoDTO("Produto Teste", "Categoria Teste", 100.0, 10);
        
        produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Produto Teste");
        produto.setCartegoria("Categoria Teste");
        produto.setPreco(100.0);
        produto.setQuantidadeEmEstoque(10);
    }

    @Test
    void testFindAll_ShouldReturnListOfProdutos() throws Exception {
        List<ProdutoDTO> produtos = new ArrayList<>();
        produtos.add(produtoDTO);

        when(produtoService.findAll()).thenReturn(produtos);

        mockMvc.perform(get("/api/v1/produto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Produto Teste"))
                .andExpect(jsonPath("$[0].preco").value(100.0));

        verify(produtoService, times(1)).findAll();
    }

    @Test
    void testFindById_ShouldReturnProduto() throws Exception {
        when(produtoService.findById(produtoId.toString())).thenReturn(produtoDTO);

        mockMvc.perform(get("/api/v1/produto/{id}", produtoId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.preco").value(100.0));

        verify(produtoService, times(1)).findById(produtoId.toString());
    }

    @Test
    void testCreate_ShouldCreateProduto() throws Exception {
        when(produtoService.create(any(ProdutoDTO.class))).thenReturn(produto);

        mockMvc.perform(post("/api/v1/produto")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Produto Teste"));

        verify(produtoService, times(1)).create(any(ProdutoDTO.class));
    }

    @Test
    void testUpdate_ShouldUpdateProduto() throws Exception {
        ProdutoDTO updatedDTO = new ProdutoDTO("Produto Atualizado", "Nova Categoria", 150.0, 20);
        
        when(produtoService.updateProduto(any(ProdutoDTO.class), eq(produtoId.toString()))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/produto/{id}", produtoId.toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"))
                .andExpect(jsonPath("$.preco").value(150.0));

        verify(produtoService, times(1)).updateProduto(any(ProdutoDTO.class), eq(produtoId.toString()));
    }

    @Test
    void testDelete_ShouldDeleteProduto() throws Exception {
        doNothing().when(produtoService).deleteById(produtoId.toString());

        mockMvc.perform(delete("/api/v1/produto/{id}", produtoId.toString())
                        .with(csrf()))
                .andExpect(status().isAccepted());

        verify(produtoService, times(1)).deleteById(produtoId.toString());
    }

    @Test
    void testCreate_ShouldReturnBadRequestForInvalidData() throws Exception {
        ProdutoDTO invalidDTO = new ProdutoDTO("", "Categoria", -10.0, -5);

        mockMvc.perform(post("/api/v1/produto")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(produtoService, never()).create(any(ProdutoDTO.class));
    }
}
