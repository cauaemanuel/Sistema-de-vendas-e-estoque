package com.sales_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_management_system.controller.dto.ClienteDTO;
import com.sales_management_system.entity.Cliente;
import com.sales_management_system.service.ClienteService;
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

@WebMvcTest(value = ClienteController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = {com.sales_management_system.security.config.SecurityConfig.class,
               com.sales_management_system.security.config.SecurityFilter.class}))
@WithMockUser
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    private ClienteDTO clienteDTO;
    private Cliente cliente;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        
        clienteDTO = new ClienteDTO("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        
        cliente = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        cliente.setId(clienteId);
    }

    @Test
    void testFindAll_ShouldReturnListOfClientes() throws Exception {
        List<ClienteDTO> clientes = new ArrayList<>();
        clientes.add(clienteDTO);

        when(clienteService.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/api/v1/cliente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].cpf").value("12345678901"));

        verify(clienteService, times(1)).findAll();
    }

    @Test
    void testFindById_ShouldReturnCliente() throws Exception {
        when(clienteService.findById(clienteId.toString())).thenReturn(clienteDTO);

        mockMvc.perform(get("/api/v1/cliente/{id}", clienteId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));

        verify(clienteService, times(1)).findById(clienteId.toString());
    }

    @Test
    void testCreate_ShouldCreateCliente() throws Exception {
        when(clienteService.create(any(ClienteDTO.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/v1/cliente")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(clienteService, times(1)).create(any(ClienteDTO.class));
    }

    @Test
    void testUpdate_ShouldUpdateCliente() throws Exception {
        ClienteDTO updatedDTO = new ClienteDTO("João Silva Atualizado", "12345678901", "(11) 99999-9999", "joao.novo@email.com");
        
        when(clienteService.updateCliente(any(ClienteDTO.class), eq(clienteId.toString()))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/cliente/{id}", clienteId.toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"))
                .andExpect(jsonPath("$.telefone").value("(11) 99999-9999"));

        verify(clienteService, times(1)).updateCliente(any(ClienteDTO.class), eq(clienteId.toString()));
    }

    @Test
    void testDelete_ShouldDeleteCliente() throws Exception {
        doNothing().when(clienteService).deleteById(clienteId.toString());

        mockMvc.perform(delete("/api/v1/cliente/{id}", clienteId.toString())
                        .with(csrf()))
                .andExpect(status().isAccepted());

        verify(clienteService, times(1)).deleteById(clienteId.toString());
    }

    @Test
    void testCreate_ShouldReturnBadRequestForInvalidData() throws Exception {
        ClienteDTO invalidDTO = new ClienteDTO("A", "123", "telefone", "email-invalido");

        mockMvc.perform(post("/api/v1/cliente")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(clienteService, never()).create(any(ClienteDTO.class));
    }
}
