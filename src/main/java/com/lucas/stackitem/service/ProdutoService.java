package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Produto;
import com.lucas.stackitem.model.ProdutoImagem;
import com.lucas.stackitem.repository.ProdutoImagemRepository;
import com.lucas.stackitem.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoImagemRepository produtoImagemRepository;

    public Produto create(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Page<Produto> findAllPaged(Pageable pageable) {
        return produtoRepository.findAll(pageable);
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
 
    public void fazerUploadImagens(Long produtoId, List<MultipartFile> arquivos) {
        Produto produto = findById(produtoId)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + produtoId));

        for (MultipartFile arquivo : arquivos) {
            try {
                if (arquivo.isEmpty()) continue;

                ProdutoImagem novaImagem = new ProdutoImagem();
                novaImagem.setNomeArquivo(arquivo.getOriginalFilename());
                novaImagem.setDadosImagem(arquivo.getBytes()); 
                novaImagem.setProduto(produto);

                produto.getImagens().add(novaImagem);

            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar os bytes do arquivo: " + arquivo.getOriginalFilename(), e);
            }
        }

      
        produtoRepository.save(produto);
    }
 
    public void deletarImagem(Long produtoId, Long imagemId) {
        Produto produto = findById(produtoId)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + produtoId));

        ProdutoImagem imagem = produtoImagemRepository.findById(imagemId)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada com o ID: " + imagemId));

         if (!imagem.getProduto().getId().equals(produtoId)) {
            throw new RuntimeException("Esta imagem não pertence ao produto informado.");
        }

        produto.getImagens().remove(imagem);
        
        produtoRepository.save(produto);
    }

}