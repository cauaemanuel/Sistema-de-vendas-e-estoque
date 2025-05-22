package com.sales_management_system.service;

import com.sales_management_system.Repository.ClienteRepository;
import com.sales_management_system.controller.dto.ClienteDTO;
import com.sales_management_system.mapper.ClienteMap;
import com.sales_management_system.entity.Cliente;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    private ClienteRepository clientRepository;
    private ClienteMap clienteMap;

    public ClienteService(ClienteRepository clientRepository, ClienteMap clienteMap) {
        this.clientRepository = clientRepository;
        this.clienteMap = clienteMap;
    }

    @Transactional
    public Cliente create(ClienteDTO dto){
        var cliente = clienteMap.fromDTO(dto);
        return clientRepository.save(cliente);
    }

    public List<ClienteDTO> findAll(){
        var clients = clientRepository.findAll().stream()
                .map(p -> clienteMap.fromClient(p)).toList();
        if(clients.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return clients;
    }

    @Transactional
    public ClienteDTO findById(String id){
        var client = findEntityByid(id);
        return clienteMap.fromClient(client);
    }

    @Transactional
    public void deleteById(String id){
        var cliente = findEntityByid(id);
        clientRepository.deleteById(cliente.getId());
    }

    public ClienteDTO updateCliente(ClienteDTO dto, String id){
        var cliente = findEntityByid(id);
        var clienteUpdate = clienteMap.updateFromDTO(dto, cliente);
        return clienteMap.fromClient(clientRepository.save(clienteUpdate));
    }

    protected Cliente findEntityByid(String id){
        var uuid = UUID.fromString(id);
        return clientRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "A produto não foi encontrada; Verifique o ID"));
    }
}
