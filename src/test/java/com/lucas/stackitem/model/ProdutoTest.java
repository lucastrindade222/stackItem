package com.lucas.stackitem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição do produto teste");
        produto.setPercentualLucro(new BigDecimal("25.50"));
        produto.setDataCriacao(LocalDateTime.now());
        produto.setTags(new ArrayList<>());
        produto.setImagens(new ArrayList<>());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, produto.getId());
        assertEquals("Produto Teste", produto.getNome());
        assertEquals("Descrição do produto teste", produto.getDescricao());
        assertEquals(new BigDecimal("25.50"), produto.getPercentualLucro());
        assertNotNull(produto.getDataCriacao());
        assertNotNull(produto.getTags());
        assertNotNull(produto.getImagens());
    }

    @Test
    void testNoArgsConstructor() {
        Produto produtoVazio = new Produto();
        assertNotNull(produtoVazio);
        assertNull(produtoVazio.getId());
        assertNull(produtoVazio.getNome());
    }

    @Test
    void testAllArgsConstructor() {
        List<Tag> tags = new ArrayList<>();
        List<String> imagens = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();
        
        Produto produtoCompleto = new Produto(
            2L,
            "Produto Completo",
            "Descrição completa",
            agora,
            null,
            new BigDecimal("30.00"),
            tags,
            imagens
        );

        assertEquals(2L, produtoCompleto.getId());
        assertEquals("Produto Completo", produtoCompleto.getNome());
        assertEquals("Descrição completa", produtoCompleto.getDescricao());
        assertEquals(agora, produtoCompleto.getDataCriacao());
        assertEquals(new BigDecimal("30.00"), produtoCompleto.getPercentualLucro());
        assertEquals(tags, produtoCompleto.getTags());
        assertEquals(imagens, produtoCompleto.getImagens());
    }

    @Test
    void testOnCreate() {
        Produto novoProduto = new Produto();
        LocalDateTime antes = LocalDateTime.now();
        novoProduto.setNome("Novo Produto");
        
        // Simula o callback @PrePersist
        invokeOnCreate(novoProduto);
        
        assertTrue(novoProduto.getDataCriacao().isAfter(antes) || 
                   novoProduto.getDataCriacao().isEqual(antes));
    }

    @Test
    void testOnUpdate() {
        Produto produtoUpdate = new Produto();
        produtoUpdate.setNome("Produto Update");
        LocalDateTime antes = LocalDateTime.now();
        
        // Simula o callback @PreUpdate
        invokeOnUpdate(produtoUpdate);
        
        assertTrue(produtoUpdate.getDataAtualizacao().isAfter(antes) || 
                   produtoUpdate.getDataAtualizacao().isEqual(antes));
    }

    @Test
    void testEquals() {
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Produto 1");

        Produto produto2 = new Produto();
        produto2.setId(1L);
        produto2.setNome("Produto 2");

        Produto produto3 = new Produto();
        produto3.setId(2L);
        produto3.setNome("Produto 3");

        assertEquals(produto1, produto2);
        assertNotEquals(produto1, produto3);
    }

    @Test
    void testHashCode() {
        Produto produto1 = new Produto();
        produto1.setId(1L);

        Produto produto2 = new Produto();
        produto2.setId(1L);

        assertEquals(produto1.hashCode(), produto2.hashCode());
    }

    @Test
    void testAddTag() {
        produto.getTags().add(new Tag(1L, "Tag1", "Descricao", LocalDateTime.now(), null));
        assertEquals(1, produto.getTags().size());
        assertEquals("Tag1", produto.getTags().get(0).getNome());
    }

    @Test
    void testAddImagem() {
        produto.getImagens().add("imagem1.jpg");
        assertEquals(1, produto.getImagens().size());
        assertEquals("imagem1.jpg", produto.getImagens().get(0));
    }

    @Test
    void testToString() {
        String toString = produto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Produto Teste"));
    }

    // Método auxiliar para invocar o método protected onCreate
    private void invokeOnCreate(Produto produto) {
        try {
            java.lang.reflect.Method method = Produto.class.getDeclaredMethod("onCreate");
            method.setAccessible(true);
            method.invoke(produto);
        } catch (Exception e) {
            fail("Erro ao invocar onCreate: " + e.getMessage());
        }
    }

    // Método auxiliar para invocar o método protected onUpdate
    private void invokeOnUpdate(Produto produto) {
        try {
            java.lang.reflect.Method method = Produto.class.getDeclaredMethod("onUpdate");
            method.setAccessible(true);
            method.invoke(produto);
        } catch (Exception e) {
            fail("Erro ao invocar onUpdate: " + e.getMessage());
        }
    }
}