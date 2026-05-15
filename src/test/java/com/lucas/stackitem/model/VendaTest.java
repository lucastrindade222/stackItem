package com.lucas.stackitem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendaTest {

    private Venda venda;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setSobrenome("Silva");
        usuario.setEmail("joao.silva@email.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(PerfilUsuario.ADMINISTRADOR);
        usuario.setStatus(StatusUsuario.ATIVO);

        venda = new Venda();
        venda.setId(1L);
        venda.setValorTotal(new BigDecimal("100.50"));
        venda.setValorLucro(new BigDecimal("25.00"));
        venda.setUsuario(usuario);
        venda.setDataCriacao(LocalDateTime.now());
        venda.setProdutos(new ArrayList<>());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, venda.getId());
        assertEquals(new BigDecimal("100.50"), venda.getValorTotal());
        assertEquals(new BigDecimal("25.00"), venda.getValorLucro());
        assertEquals(usuario, venda.getUsuario());
        assertNotNull(venda.getDataCriacao());
        assertNotNull(venda.getProdutos());
    }

    @Test
    void testNoArgsConstructor() {
        Venda vendaVazia = new Venda();
        assertNotNull(vendaVazia);
        assertNull(vendaVazia.getId());
        assertNull(vendaVazia.getValorTotal());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime agora = LocalDateTime.now();
        List<Produto> produtos = new ArrayList<>();
        
        Venda vendaCompleta = new Venda(
            2L,
            new BigDecimal("200.00"),
            usuario,
            new BigDecimal("50.00"),
            agora,
            null,
            produtos
        );

        assertEquals(2L, vendaCompleta.getId());
        assertEquals(new BigDecimal("200.00"), vendaCompleta.getValorTotal());
        assertEquals(usuario, vendaCompleta.getUsuario());
        assertEquals(new BigDecimal("50.00"), vendaCompleta.getValorLucro());
        assertEquals(agora, vendaCompleta.getDataCriacao());
        assertEquals(produtos, vendaCompleta.getProdutos());
    }

    @Test
    void testOnCreate() {
        Venda novaVenda = new Venda();
        LocalDateTime antes = LocalDateTime.now();
        novaVenda.setValorTotal(new BigDecimal("50.00"));
        novaVenda.setUsuario(usuario);
        
        // Simula o callback @PrePersist
        invokeOnCreate(novaVenda);
        
        assertTrue(novaVenda.getDataCriacao().isAfter(antes) || 
                   novaVenda.getDataCriacao().isEqual(antes));
    }

    @Test
    void testOnUpdate() {
        Venda vendaUpdate = new Venda();
        vendaUpdate.setValorTotal(new BigDecimal("75.00"));
        vendaUpdate.setUsuario(usuario);
        LocalDateTime antes = LocalDateTime.now();
        
        // Simula o callback @PreUpdate
        invokeOnUpdate(vendaUpdate);
        
        assertTrue(vendaUpdate.getDataAtualizacao().isAfter(antes) || 
                   vendaUpdate.getDataAtualizacao().isEqual(antes));
    }

    @Test
    void testEquals() {
        Venda venda1 = new Venda();
        venda1.setId(1L);

        Venda venda2 = new Venda();
        venda2.setId(1L);

        Venda venda3 = new Venda();
        venda3.setId(2L);

        assertEquals(venda1, venda2);
        assertNotEquals(venda1, venda3);
    }

    @Test
    void testHashCode() {
        Venda venda1 = new Venda();
        venda1.setId(1L);

        Venda venda2 = new Venda();
        venda2.setId(1L);

        assertEquals(venda1.hashCode(), venda2.hashCode());
    }

    @Test
    void testAddProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto 1");
        venda.getProdutos().add(produto);
        assertEquals(1, venda.getProdutos().size());
        assertEquals("Produto 1", venda.getProdutos().get(0).getNome());
    }

    @Test
    void testToString() {
        String toString = venda.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("100.50"));
    }

    // Método auxiliar para invocar o método protected onCreate
    private void invokeOnCreate(Venda venda) {
        try {
            java.lang.reflect.Method method = Venda.class.getDeclaredMethod("onCreate");
            method.setAccessible(true);
            method.invoke(venda);
        } catch (Exception e) {
            fail("Erro ao invocar onCreate: " + e.getMessage());
        }
    }

    // Método auxiliar para invocar o método protected onUpdate
    private void invokeOnUpdate(Venda venda) {
        try {
            java.lang.reflect.Method method = Venda.class.getDeclaredMethod("onUpdate");
            method.setAccessible(true);
            method.invoke(venda);
        } catch (Exception e) {
            fail("Erro ao invocar onUpdate: " + e.getMessage());
        }
    }
}