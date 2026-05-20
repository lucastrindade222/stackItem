package com.lucas.stackitem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerfilTest {

    private Perfil perfil;

    @BeforeEach
    void setUp() {
        perfil = new Perfil(1L, "ADMINISTRADOR");
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, perfil.getId());
        assertEquals("ADMINISTRADOR", perfil.getNome());

        perfil.setId(2L);
        perfil.setNome("VENDEDOR");

        assertEquals(2L, perfil.getId());
        assertEquals("VENDEDOR", perfil.getNome());
    }

    @Test
    void testNoArgsConstructor() {
        Perfil perfilVazio = new Perfil();
        assertNotNull(perfilVazio);
        assertNull(perfilVazio.getId());
        assertNull(perfilVazio.getNome());
    }

    @Test
    void testAllArgsConstructor() {
        Perfil perfilCompleto = new Perfil(3L, "CLIENTE");
        assertEquals(3L, perfilCompleto.getId());
        assertEquals("CLIENTE", perfilCompleto.getNome());
    }

    @Test
    void testEquals() {
        Perfil perfil1 = new Perfil(1L, "ADMINISTRADOR");
        Perfil perfil2 = new Perfil(1L, "ADMINISTRADOR");
        Perfil perfil3 = new Perfil(3L, "CLIENTE");

        assertEquals(perfil1, perfil2);
        assertNotEquals(perfil1, perfil3);
    }

    @Test
    void testHashCode() {
        Perfil perfil1 = new Perfil(1L, "ADMINISTRADOR");
        Perfil perfil2 = new Perfil(1L, "ADMINISTRADOR");

        assertEquals(perfil1.hashCode(), perfil2.hashCode());
    }

    @Test
    void testToString() {
        String toString = perfil.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ADMINISTRADOR"));
    }
}