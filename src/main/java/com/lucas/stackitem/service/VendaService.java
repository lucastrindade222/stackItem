package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Venda;
import com.lucas.stackitem.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    public Venda create(Venda venda) {
        return vendaRepository.save(venda);
    }

    public Optional<Venda> findById(Long id) {
        return vendaRepository.findById(id);
    }

    public List<Venda> findAll() {
        return vendaRepository.findAll();
    }

    public Page<Venda> findAllPaged(Pageable pageable) {
        return vendaRepository.findAll(pageable);
    }

    public Page<Venda> findByUsuarioId(Long usuarioId,Pageable pageable) {
        return vendaRepository.findByUsuarioId(usuarioId, pageable);
    }

    public List<Venda> findByUsuarioId(Long usuarioId) {
        return vendaRepository.findByUsuarioId(usuarioId);
    }

    public Venda update(Long id, Venda venda) {
        Venda existingVenda = vendaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));
        
        venda.setId(id);
        return vendaRepository.save(venda);
    }

    public void delete(Long id) {
        if (!vendaRepository.existsById(id)) {
            throw new IllegalArgumentException("Venda não encontrada");
        }
        vendaRepository.deleteById(id);
    }
}