package com.lucas.stackitem.controller;

import com.lucas.stackitem.model.Venda;
import com.lucas.stackitem.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<Venda> create(@RequestBody Venda venda) {
        Venda createdVenda = vendaService.create(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVenda);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> findById(@PathVariable Long id) {
        return vendaService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Venda>> findAll() {
        List<Venda> vendas = vendaService.findAll();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<Venda>> findByUsuarioId(@PathVariable Long usuarioId) {
        List<Venda> vendas = vendaService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(vendas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venda> update(@PathVariable Long id, @RequestBody Venda venda) {
        try {
            Venda updatedVenda = vendaService.update(id, venda);
            return ResponseEntity.ok(updatedVenda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            vendaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}