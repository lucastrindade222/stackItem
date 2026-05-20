package com.lucas.stackitem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;
    private Perfil perfilAdministrador;
    private Perfil perfilVendedor;
    private Perfil perfilCliente;

    @BeforeEach
    void setUp() {
        perfilAdministrador = new Perfil(1L, "ADMINISTRADOR");
        perfilVendedor = new Perfil(2L, "VENDEDOR");
        perfilCliente = new Perfil(3L, "CLIENTE");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setSobrenome("Silva");
        usuario.setEmail("joao.silva@email.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(perfilAdministrador);
        usuario.setStatus(StatusUsuario.ATIVO);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setVendas(new ArrayList<>());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, usuario.getId());
        assertEquals("João", usuario.getNome());
        assertEquals("Silva", usuario.getSobrenome());
        assertEquals("joao.silva@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        assertEquals(perfilAdministrador, usuario.getPerfil());
        assertEquals(StatusUsuario.ATIVO, usuario.getStatus());
        assertNotNull(usuario.getDataCriacao());
        assertNotNull(usuario.getVendas());
    }

    @Test
    void testNoArgsConstructor() {
        Usuario usuarioVazio = new Usuario();
        assertNotNull(usuarioVazio);
        assertNull(usuarioVazio.getId());
        assertNull(usuarioVazio.getNome());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime agora = LocalDateTime.now();
        List<Venda> vendas = new ArrayList<>();
        byte[] imagemPerfil = new byte[]{0x01, 0x02, 0x03};

        Usuario usuarioCompleto = new Usuario(
            2L,
            "Maria",
            "Santos",
            imagemPerfil,
            agora,
            null,
            StatusUsuario.INATIVO,
            "maria.santos@email.com",
            "senha456",
            perfilVendedor,
            vendas
        );

        assertEquals(2L, usuarioCompleto.getId());
        assertEquals("Maria", usuarioCompleto.getNome());
        assertEquals("Santos", usuarioCompleto.getSobrenome());
        assertArrayEquals(imagemPerfil, usuarioCompleto.getImagemPerfil());
        assertEquals(agora, usuarioCompleto.getDataCriacao());
        assertEquals(StatusUsuario.INATIVO, usuarioCompleto.getStatus());
        assertEquals("maria.santos@email.com", usuarioCompleto.getEmail());
        assertEquals("senha456", usuarioCompleto.getSenha());
        assertEquals(perfilVendedor, usuarioCompleto.getPerfil());
        assertEquals(vendas, usuarioCompleto.getVendas());
    }

    @Test
    void testOnCreate() {
        Usuario novoUsuario = new Usuario();
        LocalDateTime antes = LocalDateTime.now();
        novoUsuario.setNome("Novo Usuário");
        novoUsuario.setEmail("novo@email.com");
        novoUsuario.setSenha("senha");
        novoUsuario.setPerfil(perfilCliente);

        // Simula o callback @PrePersist
        invokeOnCreate(novoUsuario);

        assertTrue(novoUsuario.getDataCriacao().isAfter(antes) ||
                   novoUsuario.getDataCriacao().isEqual(antes));
        assertEquals(StatusUsuario.ATIVO, novoUsuario.getStatus());
    }

    @Test
    void testOnUpdate() {
        Usuario usuarioUpdate = new Usuario();
        usuarioUpdate.setNome("Usuário Update");
        usuarioUpdate.setEmail("update@email.com");
        usuarioUpdate.setSenha("senha");
        usuarioUpdate.setPerfil(perfilCliente);
        LocalDateTime antes = LocalDateTime.now();

        // Simula o callback @PreUpdate
        invokeOnUpdate(usuarioUpdate);

        assertTrue(usuarioUpdate.getDataAtualizacao().isAfter(antes) ||
                   usuarioUpdate.getDataAtualizacao().isEqual(antes));
    }

    @Test
    void testEquals() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Usuario 1");

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNome("Usuario 2");

        Usuario usuario3 = new Usuario();
        usuario3.setId(2L);
        usuario3.setNome("Usuario 3");

        assertEquals(usuario1, usuario2);
        assertNotEquals(usuario1, usuario3);
    }

    @Test
    void testHashCode() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);

        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void testAddVenda() {
        Venda venda = new Venda();
        venda.setId(1L);
        usuario.getVendas().add(venda);
        assertEquals(1, usuario.getVendas().size());
    }

    @Test
    void testStatusUsuarioEnum() {
        assertEquals(StatusUsuario.ATIVO, StatusUsuario.valueOf("ATIVO"));
        assertEquals(StatusUsuario.INATIVO, StatusUsuario.valueOf("INATIVO"));
        assertEquals(StatusUsuario.SUSPENSO, StatusUsuario.valueOf("SUSPENSO"));
    }

    @Test
    void testPerfilUsuario() {
        assertNotNull(perfilAdministrador);
        assertEquals("ADMINISTRADOR", perfilAdministrador.getNome());
        assertNotNull(perfilVendedor);
        assertEquals("VENDEDOR", perfilVendedor.getNome());
        assertNotNull(perfilCliente);
        assertEquals("CLIENTE", perfilCliente.getNome());
    }

    @Test
    void testToString() {
        String toString = usuario.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("João"));
    }

    // Método auxiliar para invocar o método protected onCreate
    private void invokeOnCreate(Usuario usuario) {
        try {
            java.lang.reflect.Method method = Usuario.class.getDeclaredMethod("onCreate");
            method.setAccessible(true);
            method.invoke(usuario);
        } catch (Exception e) {
            fail("Erro ao invocar onCreate: " + e.getMessage());
        }
    }

    // Método auxiliar para invocar o método protected onUpdate
    private void invokeOnUpdate(Usuario usuario) {
        try {
            java.lang.reflect.Method method = Usuario.class.getDeclaredMethod("onUpdate");
            method.setAccessible(true);
            method.invoke(usuario);
        } catch (Exception e) {
            fail("Erro ao invocar onUpdate: " + e.getMessage());
        }
    }
}