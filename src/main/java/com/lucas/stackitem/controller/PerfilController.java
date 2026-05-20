package com.lucas.stackitem.controller;

import com.lucas.stackitem.model.Perfil;
import com.lucas.stackitem.service.PerfilService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/perfils")
@Tag(name = "Perfils", description = "Operações relacionadas a Perfils")
public class PerfilController {

    @Autowired
    private PerfilService PerfilService;

    @PostMapping
    public ResponseEntity<Perfil> create(@RequestBody Perfil Perfil) {
        Perfil createdPerfil = PerfilService.create(Perfil);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerfil);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Perfil> findById(@PathVariable Long id) {
        return PerfilService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Perfil>> findAll() {
        List<Perfil> Perfils = PerfilService.findAll();
        return ResponseEntity.ok(Perfils);
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<Perfil> update(@PathVariable Long id, @RequestBody Perfil Perfil) {
        try {
            Perfil updatedPerfil = PerfilService.update(id, Perfil);
            return ResponseEntity.ok(updatedPerfil);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            PerfilService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}