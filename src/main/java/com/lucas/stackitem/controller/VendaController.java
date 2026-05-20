package com.lucas.stackitem.controller;

import com.lucas.stackitem.model.Venda;
import com.lucas.stackitem.service.VendaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import java.util.List;

@RestController
@RequestMapping("/sales")
@Tag(name = "Vendas", description = "Operações relacionadas a vendas")
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

    @GetMapping("/paged")
    public ResponseEntity<Page<Venda>> findAllPaged(@PageableDefault(page = 0, size = 10, sort = "id",
     direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Venda> vendas = vendaService.findAllPaged(pageable);
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<Venda>> findByUsuarioId(@PathVariable Long usuarioId) {
        List<Venda> vendas = vendaService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/user/paged/{usuarioId}")
    public ResponseEntity<Page<Venda>> findByUsuarioId(@PathVariable Long usuarioId,
        @PageableDefault(page = 0, size = 10, sort = "id",
     direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Venda> vendas = vendaService.findByUsuarioId(usuarioId, pageable);
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