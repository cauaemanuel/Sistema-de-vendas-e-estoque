package com.sales_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_management_system.controller.dto.ProdutoVendaDTO;
import com.sales_management_system.controller.dto.ReciboDTO;
import com.sales_management_system.controller.dto.VendaDTO;
import com.sales_management_system.service.VendaService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = VendaController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = {com.sales_management_system.security.config.SecurityConfig.class,
               com.sales_management_system.security.config.SecurityFilter.class}))
@WithMockUser
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VendaService vendaService;

    private VendaDTO vendaDTO;
    private ReciboDTO reciboDTO;
    private UUID vendaId;
    private UUID clienteId;
    private UUID produtoId;

    @BeforeEach
    void setUp() {
        vendaId = UUID.randomUUID();
        clienteId = UUID.randomUUID();
        produtoId = UUID.randomUUID();

        ProdutoVendaDTO produtoVendaDTO = new ProdutoVendaDTO(produtoId.toString(), 2);
        List<ProdutoVendaDTO> produtos = new ArrayList<>();
        produtos.add(produtoVendaDTO);

        vendaDTO = new VendaDTO(clienteId.toString(), LocalDateTime.now(), produtos);

        reciboDTO = new ReciboDTO(
                "João Silva",
                vendaId.toString(),
                LocalDateTime.now(),
                200.0,
                new ArrayList<>()
        );
    }

    @Test
    void testRealizarVenda_ShouldCreateVenda() throws Exception {
        when(vendaService.realizarVenda(any(VendaDTO.class))).thenReturn(reciboDTO);

        mockMvc.perform(post("/api/v1/venda")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.ValorTotal").value(200.0));

        verify(vendaService, times(1)).realizarVenda(any(VendaDTO.class));
    }

    @Test
    void testBuscarReciboPorIdVenda_ShouldReturnRecibo() throws Exception {
        when(vendaService.buscarReciboPorIdVenda(vendaId.toString())).thenReturn(reciboDTO);

        mockMvc.perform(get("/api/v1/venda/{id}/recibo", vendaId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.idVenda").value(vendaId.toString()));

        verify(vendaService, times(1)).buscarReciboPorIdVenda(vendaId.toString());
    }

    @Test
    void testRealizarVenda_ShouldReturnBadRequestForInvalidData() throws Exception {
        VendaDTO invalidDTO = new VendaDTO("", null, new ArrayList<>());

        mockMvc.perform(post("/api/v1/venda")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(vendaService, never()).realizarVenda(any(VendaDTO.class));
    }
}
