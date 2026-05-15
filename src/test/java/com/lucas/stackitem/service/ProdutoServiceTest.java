package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Produto;
import com.lucas.stackitem.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição do produto");
        produto.setPercentualLucro(new BigDecimal("25.50"));
    }

    @Test
    void testCreate() {
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = produtoService.create(produto);

        assertNotNull(resultado);
        assertEquals("Produto Teste", resultado.getNome());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void testFindById() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Produto Teste", resultado.get().getNome());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Produto> resultado = produtoService.findById(99L);

        assertFalse(resultado.isPresent());
        verify(produtoRepository, times(1)).findById(99L);
    }

    @Test
    void testFindAll() {
        List<Produto> produtos = Arrays.asList(produto, new Produto());
        when(produtoRepository.findAll()).thenReturn(produtos);

        List<Produto> resultado = produtoService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(produtoRepository.findAll()).thenReturn(Arrays.asList());

        List<Produto> resultado = produtoService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Produto Atualizado");
        produtoAtualizado.setDescricao("Nova descrição");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        Produto resultado = produtoService.update(1L, produtoAtualizado);

        assertNotNull(resultado);
        assertEquals("Produto Atualizado", resultado.getNome());
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testUpdateNotFound() {
        Produto produtoAtualizado = new Produto();
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> produtoService.update(99L, produtoAtualizado)
        );

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(produtoRepository, times(1)).findById(99L);
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void testDelete() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        produtoService.delete(1L);

        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(produtoRepository.existsById(99L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> produtoService.delete(99L)
        );

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(produtoRepository, times(1)).existsById(99L);
        verify(produtoRepository, never()).deleteById(anyLong());
    }
}