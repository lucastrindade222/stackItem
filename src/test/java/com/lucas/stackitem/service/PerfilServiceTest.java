package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Perfil;
import com.lucas.stackitem.repository.PerfilRepository;
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

class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private PerfilService perfilService;

    private Perfil perfil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        perfil = new Perfil(1L, "ADMINISTRADOR");
    }

    @Test
    void testCreate() {
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        Perfil resultado = perfilService.create(perfil);

        assertNotNull(resultado);
        assertEquals("ADMINISTRADOR", resultado.getNome());
        verify(perfilRepository, times(1)).save(perfil);
    }

    @Test
    void testListarTodos() {
        List<Perfil> perfis = Arrays.asList(perfil, new Perfil(2L, "VENDEDOR"));
        when(perfilRepository.findAll()).thenReturn(perfis);

        List<Perfil> resultado = perfilService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(perfilRepository, times(1)).findAll();
    }

    @Test
    void testListarTodosEmpty() {
        when(perfilRepository.findAll()).thenReturn(Arrays.asList());

        List<Perfil> resultado = perfilService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(perfilRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));

        Optional<Perfil> resultado = perfilService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("ADMINISTRADOR", resultado.get().getNome());
        verify(perfilRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Perfil> resultado = perfilService.findById(99L);

        assertFalse(resultado.isPresent());
        verify(perfilRepository, times(1)).findById(99L);
    }

    @Test
    void testFindAll() {
        List<Perfil> perfis = Arrays.asList(perfil, new Perfil(2L, "VENDEDOR"));
        when(perfilRepository.findAll()).thenReturn(perfis);

        List<Perfil> resultado = perfilService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(perfilRepository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(perfilRepository.findAll()).thenReturn(Arrays.asList());

        List<Perfil> resultado = perfilService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(perfilRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        Perfil perfilAtualizado = new Perfil(1L, "VENDEDOR");

        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfilAtualizado);

        Perfil resultado = perfilService.update(1L, perfilAtualizado);

        assertNotNull(resultado);
        assertEquals("VENDEDOR", resultado.getNome());
        verify(perfilRepository, times(1)).findById(1L);
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }

    @Test
    void testUpdateNotFound() {
        Perfil perfilAtualizado = new Perfil(99L, "INEXISTENTE");
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> perfilService.update(99L, perfilAtualizado)
        );

        assertEquals("Perfil não encontrado", exception.getMessage());
        verify(perfilRepository, times(1)).findById(99L);
        verify(perfilRepository, never()).save(any(Perfil.class));
    }

    @Test
    void testDelete() {
        when(perfilRepository.existsById(1L)).thenReturn(true);

        perfilService.delete(1L);

        verify(perfilRepository, times(1)).existsById(1L);
        verify(perfilRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(perfilRepository.existsById(99L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> perfilService.delete(99L)
        );

        assertEquals("Perfil não encontrado", exception.getMessage());
        verify(perfilRepository, times(1)).existsById(99L);
        verify(perfilRepository, never()).deleteById(anyLong());
    }
}