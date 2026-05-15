package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Produto;
import com.lucas.stackitem.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto create(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Produto update(Long id, Produto produto) {
        Produto existingProduto = produtoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
        produto.setId(id);
        return produtoRepository.save(produto);
    }

    public void delete(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }
}