package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Tag;
import com.lucas.stackitem.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private Tag tag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        tag = new Tag();
        tag.setId(1L);
        tag.setNome("Tag Teste");
        tag.setDescricao("Descrição da tag");
    }

    @Test
    void testCreate() {
        when(tagRepository.existsByNome(tag.getNome())).thenReturn(false);
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag resultado = tagService.create(tag);

        assertNotNull(resultado);
        assertEquals("Tag Teste", resultado.getNome());
        verify(tagRepository, times(1)).existsByNome(tag.getNome());
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    void testCreateDuplicateTag() {
        when(tagRepository.existsByNome(tag.getNome())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tagService.create(tag)
        );

        assertEquals("Tag já cadastrada", exception.getMessage());
        verify(tagRepository, times(1)).existsByNome(tag.getNome());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void testFindById() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        Optional<Tag> resultado = tagService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Tag Teste", resultado.get().getNome());
        verify(tagRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Tag> resultado = tagService.findById(99L);

        assertFalse(resultado.isPresent());
        verify(tagRepository, times(1)).findById(99L);
    }

    @Test
    void testFindByNome() {
        when(tagRepository.findByNome("Tag Teste")).thenReturn(Optional.of(tag));

        Optional<Tag> resultado = tagService.findByNome("Tag Teste");

        assertTrue(resultado.isPresent());
        assertEquals("Tag Teste", resultado.get().getNome());
        verify(tagRepository, times(1)).findByNome("Tag Teste");
    }

    @Test
    void testFindByNomeNotFound() {
        when(tagRepository.findByNome("Tag Inexistente")).thenReturn(Optional.empty());

        Optional<Tag> resultado = tagService.findByNome("Tag Inexistente");

        assertFalse(resultado.isPresent());
        verify(tagRepository, times(1)).findByNome("Tag Inexistente");
    }

    @Test
    void testFindAll() {
        List<Tag> tags = Arrays.asList(tag, new Tag());
        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> resultado = tagService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(tagRepository.findAll()).thenReturn(Arrays.asList());

        List<Tag> resultado = tagService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        Tag tagAtualizada = new Tag();
        tagAtualizada.setNome("Tag Atualizada");
        tagAtualizada.setDescricao("Nova descrição");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByNome(tagAtualizada.getNome())).thenReturn(false);
        when(tagRepository.save(any(Tag.class))).thenReturn(tagAtualizada);

        Tag resultado = tagService.update(1L, tagAtualizada);

        assertNotNull(resultado);
        assertEquals("Tag Atualizada", resultado.getNome());
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).existsByNome(tagAtualizada.getNome());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void testUpdateNotFound() {
        Tag tagAtualizada = new Tag();
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tagService.update(99L, tagAtualizada)
        );

        assertEquals("Tag não encontrada", exception.getMessage());
        verify(tagRepository, times(1)).findById(99L);
        verify(tagRepository, never()).existsByNome(anyString());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void testUpdateDuplicateTag() {
        Tag tagAtualizada = new Tag();
        tagAtualizada.setNome("Tag Existente");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByNome(tagAtualizada.getNome())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tagService.update(1L, tagAtualizada)
        );

        assertEquals("Tag já cadastrada", exception.getMessage());
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).existsByNome(tagAtualizada.getNome());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void testUpdateSameTagSameNome() {
        Tag tagAtualizada = new Tag();
        tagAtualizada.setNome("Tag Teste");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenReturn(tagAtualizada);

        Tag resultado = tagService.update(1L, tagAtualizada);

        assertNotNull(resultado);
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, never()).existsByNome(anyString());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void testDelete() {
        when(tagRepository.existsById(1L)).thenReturn(true);

        tagService.delete(1L);

        verify(tagRepository, times(1)).existsById(1L);
        verify(tagRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(tagRepository.existsById(99L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tagService.delete(99L)
        );

        assertEquals("Tag não encontrada", exception.getMessage());
        verify(tagRepository, times(1)).existsById(99L);
        verify(tagRepository, never()).deleteById(anyLong());
    }
}