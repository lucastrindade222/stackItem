package com.lucas.stackitem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoImagemTest {

    private ProdutoImagem imagem;
    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");

        imagem = new ProdutoImagem();
        imagem.setId(1L);
        imagem.setNomeArquivo("foto.jpg");
        imagem.setDadosImagem(new byte[]{0x01, 0x02, 0x03});
        imagem.setProduto(produto);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, imagem.getId());
        assertEquals("foto.jpg", imagem.getNomeArquivo());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, imagem.getDadosImagem());
        assertEquals(produto, imagem.getProduto());
    }

    @Test
    void testNoArgsConstructor() {
        ProdutoImagem imagemVazia = new ProdutoImagem();
        assertNotNull(imagemVazia);
        assertNull(imagemVazia.getId());
        assertNull(imagemVazia.getNomeArquivo());
    }

    @Test
    void testAllArgsConstructor() {
        byte[] dados = new byte[]{0x0A, 0x0B};
        ProdutoImagem imagemCompleta = new ProdutoImagem(2L, "imagem.png", dados, produto);
        assertEquals(2L, imagemCompleta.getId());
        assertEquals("imagem.png", imagemCompleta.getNomeArquivo());
        assertArrayEquals(dados, imagemCompleta.getDadosImagem());
        assertEquals(produto, imagemCompleta.getProduto());
    }

    @Test
    void testEquals() {
        ProdutoImagem img1 = new ProdutoImagem();
        img1.setId(1L);

        ProdutoImagem img2 = new ProdutoImagem();
        img2.setId(1L);

        ProdutoImagem img3 = new ProdutoImagem();
        img3.setId(3L);

        assertEquals(img1, img2);
        assertNotEquals(img1, img3);
    }

    @Test
    void testHashCode() {
        ProdutoImagem img1 = new ProdutoImagem();
        img1.setId(1L);

        ProdutoImagem img2 = new ProdutoImagem();
        img2.setId(1L);

        assertEquals(img1.hashCode(), img2.hashCode());
    }

    @Test
    void testToString() {
        String toString = imagem.toString();
        assertNotNull(toString);
    }
}