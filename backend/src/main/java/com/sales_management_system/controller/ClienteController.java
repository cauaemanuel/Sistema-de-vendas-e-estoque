package com.sales_management_system.controller;

import com.sales_management_system.controller.dto.ClienteDTO;
import com.sales_management_system.entity.Cliente;
import com.sales_management_system.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll(){
        var list = clienteService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable String id){
        var cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> create(@RequestBody @Valid ClienteDTO dto){
        var cliente = clienteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@RequestBody @Valid ClienteDTO dto, @PathVariable String id){
        var cliente = clienteService.updateCliente(dto, id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        clienteService.deleteById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}