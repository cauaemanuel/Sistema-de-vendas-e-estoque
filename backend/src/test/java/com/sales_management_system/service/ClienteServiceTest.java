package com.sales_management_system.service;

import com.sales_management_system.Repository.ClienteRepository;
import com.sales_management_system.controller.dto.ClienteDTO;
import com.sales_management_system.entity.Cliente;
import com.sales_management_system.mapper.ClienteMap;
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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clientRepository;

    @Mock
    private ClienteMap clienteMap;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        
        cliente = new Cliente("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
        cliente.setId(clienteId);

        clienteDTO = new ClienteDTO("João Silva", "12345678901", "(11) 98765-4321", "joao@email.com");
    }

    @Test
    void testCreate_ShouldCreateCliente() {
        when(clienteMap.fromDTO(clienteDTO)).thenReturn(cliente);
        when(clientRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.create(clienteDTO);

        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
        assertEquals("12345678901", result.getCpf());
        verify(clientRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testFindAll_ShouldReturnListOfClientes() {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente);

        when(clientRepository.findAll()).thenReturn(clientes);
        when(clienteMap.fromClient(any(Cliente.class))).thenReturn(clienteDTO);

        List<ClienteDTO> result = clienteService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ShouldThrowExceptionWhenEmpty() {
        when(clientRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> clienteService.findAll());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ShouldReturnCliente() {
        when(clientRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(clienteMap.fromClient(cliente)).thenReturn(clienteDTO);

        ClienteDTO result = clienteService.findById(clienteId.toString());

        assertNotNull(result);
        assertEquals("João Silva", result.nome());
        verify(clientRepository, times(1)).findById(clienteId);
    }

    @Test
    void testFindById_ShouldThrowExceptionWhenNotFound() {
        when(clientRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> clienteService.findById(clienteId.toString()));
        verify(clientRepository, times(1)).findById(clienteId);
    }

    @Test
    void testDeleteById_ShouldDeleteCliente() {
        when(clientRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        doNothing().when(clientRepository).deleteById(clienteId);

        clienteService.deleteById(clienteId.toString());

        verify(clientRepository, times(1)).findById(clienteId);
        verify(clientRepository, times(1)).deleteById(clienteId);
    }

    @Test
    void testUpdateCliente_ShouldUpdateAndReturnCliente() {
        ClienteDTO updatedDTO = new ClienteDTO("João Silva Atualizado", "12345678901", "(11) 99999-9999", "joao.novo@email.com");
        Cliente updatedCliente = new Cliente("João Silva Atualizado", "12345678901", "(11) 99999-9999", "joao.novo@email.com");
        updatedCliente.setId(clienteId);

        when(clientRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(clienteMap.updateFromDTO(updatedDTO, cliente)).thenReturn(updatedCliente);
        when(clientRepository.save(updatedCliente)).thenReturn(updatedCliente);
        when(clienteMap.fromClient(updatedCliente)).thenReturn(updatedDTO);

        ClienteDTO result = clienteService.updateCliente(updatedDTO, clienteId.toString());

        assertNotNull(result);
        assertEquals("João Silva Atualizado", result.nome());
        assertEquals("(11) 99999-9999", result.telefone());
        verify(clientRepository, times(1)).save(updatedCliente);
    }

    @Test
    void testFindEntityById_ShouldReturnCliente() {
        when(clientRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.findEntityByid(clienteId.toString());

        assertNotNull(result);
        assertEquals(clienteId, result.getId());
        verify(clientRepository, times(1)).findById(clienteId);
    }
}
