package com.sales_management_system.controller;

import com.sales_management_system.controller.dto.ReciboDTO;
import com.sales_management_system.controller.dto.VendaDTO;
import com.sales_management_system.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/venda")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<ReciboDTO> realizarVenda(@RequestBody @Valid VendaDTO vendaDTO) {
        var recibo = vendaService.realizarVenda(vendaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(recibo);
    }

    @GetMapping("/{id}/recibo")
    public ResponseEntity<ReciboDTO> buscarReciboPorIdVenda(@PathVariable String id) {
        var recibo = vendaService.buscarReciboPorIdVenda(id);
        return ResponseEntity.ok(recibo);
    }
}