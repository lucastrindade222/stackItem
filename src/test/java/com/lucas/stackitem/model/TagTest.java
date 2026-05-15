package com.lucas.stackitem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setId(1L);
        tag.setNome("Tag Teste");
        tag.setDescricao("Descrição da tag teste");
        tag.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, tag.getId());
        assertEquals("Tag Teste", tag.getNome());
        assertEquals("Descrição da tag teste", tag.getDescricao());
        assertNotNull(tag.getDataCriacao());
    }

    @Test
    void testNoArgsConstructor() {
        Tag tagVazia = new Tag();
        assertNotNull(tagVazia);
        assertNull(tagVazia.getId());
        assertNull(tagVazia.getNome());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime agora = LocalDateTime.now();
        
        Tag tagCompleta = new Tag(
            2L,
            "Tag Completa",
            "Descrição completa",
            agora,
            null
        );

        assertEquals(2L, tagCompleta.getId());
        assertEquals("Tag Completa", tagCompleta.getNome());
        assertEquals("Descrição completa", tagCompleta.getDescricao());
        assertEquals(agora, tagCompleta.getDataCriacao());
    }

    @Test
    void testOnCreate() {
        Tag novaTag = new Tag();
        LocalDateTime antes = LocalDateTime.now();
        novaTag.setNome("Nova Tag");
        
        // Simula o callback @PrePersist
        invokeOnCreate(novaTag);
        
        assertTrue(novaTag.getDataCriacao().isAfter(antes) || 
                   novaTag.getDataCriacao().isEqual(antes));
    }

    @Test
    void testOnUpdate() {
        Tag tagUpdate = new Tag();
        tagUpdate.setNome("Tag Update");
        LocalDateTime antes = LocalDateTime.now();
        
        // Simula o callback @PreUpdate
        invokeOnUpdate(tagUpdate);
        
        assertTrue(tagUpdate.getDataAtualizacao().isAfter(antes) || 
                   tagUpdate.getDataAtualizacao().isEqual(antes));
    }

    @Test
    void testEquals() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setNome("Tag 1");

        Tag tag2 = new Tag();
        tag2.setId(1L);
        tag2.setNome("Tag 2");

        Tag tag3 = new Tag();
        tag3.setId(2L);
        tag3.setNome("Tag 3");

        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
    }

    @Test
    void testHashCode() {
        Tag tag1 = new Tag();
        tag1.setId(1L);

        Tag tag2 = new Tag();
        tag2.setId(1L);

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testToString() {
        String toString = tag.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Tag Teste"));
    }

    // Método auxiliar para invocar o método protected onCreate
    private void invokeOnCreate(Tag tag) {
        try {
            java.lang.reflect.Method method = Tag.class.getDeclaredMethod("onCreate");
            method.setAccessible(true);
            method.invoke(tag);
        } catch (Exception e) {
            fail("Erro ao invocar onCreate: " + e.getMessage());
        }
    }

    // Método auxiliar para invocar o método protected onUpdate
    private void invokeOnUpdate(Tag tag) {
        try {
            java.lang.reflect.Method method = Tag.class.getDeclaredMethod("onUpdate");
            method.setAccessible(true);
            method.invoke(tag);
        } catch (Exception e) {
            fail("Erro ao invocar onUpdate: " + e.getMessage());
        }
    }
}