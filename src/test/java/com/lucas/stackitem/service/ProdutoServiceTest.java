package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Produto;
import com.lucas.stackitem.model.ProdutoImagem;
import com.lucas.stackitem.repository.ProdutoImagemRepository;
import com.lucas.stackitem.repository.ProdutoRepository;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
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

import java.io.IOException;
import java.util.ArrayList;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoImagemRepository produtoImagemRepository;

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
    void testUploadImagens() throws IOException {
        Long produtoId = 1L;
        MultipartFile arquivo1 = new MockMultipartFile("foto1", "foto1.jpg", "image/jpeg", new byte[]{0x01, 0x02});
        MultipartFile arquivo2 = new MockMultipartFile("foto2", "foto2.png", "image/png", new byte[]{0x03, 0x04});
        List<MultipartFile> arquivos = Arrays.asList(arquivo1, arquivo2);

        produto.setImagens(new ArrayList<>());
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        produtoService.fazerUploadImagens(produtoId, arquivos);

        assertEquals(2, produto.getImagens().size());
        assertEquals("foto1.jpg", produto.getImagens().get(0).getNomeArquivo());
        assertEquals("foto2.png", produto.getImagens().get(1).getNomeArquivo());
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void testUploadImagensProdutoNotFound() {
        Long produtoId = 99L;
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> produtoService.fazerUploadImagens(produtoId, Arrays.asList())
        );

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void testDeletarImagem() {
        Long produtoId = 1L;
        Long imagemId = 10L;

        ProdutoImagem imagem = new ProdutoImagem();
        imagem.setId(imagemId);
        imagem.setProduto(produto);

        produto.setImagens(new ArrayList<>());
        produto.getImagens().add(imagem);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoImagemRepository.findById(imagemId)).thenReturn(Optional.of(imagem));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        produtoService.deletarImagem(produtoId, imagemId);

        assertTrue(produto.getImagens().isEmpty());
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoImagemRepository, times(1)).findById(imagemId);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void testDeletarImagemProdutoNotFound() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> produtoService.deletarImagem(99L, 1L)
        );

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
        verify(produtoRepository, times(1)).findById(99L);
        verify(produtoImagemRepository, never()).findById(anyLong());
    }

    @Test
    void testDeletarImagemNaoPertenceAoProduto() {
        Long produtoId = 1L;
        Long imagemId = 10L;

        Produto outroProduto = new Produto();
        outroProduto.setId(2L);

        ProdutoImagem imagem = new ProdutoImagem();
        imagem.setId(imagemId);
        imagem.setProduto(outroProduto);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoImagemRepository.findById(imagemId)).thenReturn(Optional.of(imagem));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> produtoService.deletarImagem(produtoId, imagemId)
        );

        assertTrue(exception.getMessage().contains("Esta imagem não pertence ao produto informado"));
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoImagemRepository, times(1)).findById(imagemId);
        verify(produtoRepository, never()).save(any(Produto.class));
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