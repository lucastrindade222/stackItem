package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Perfil;
import com.lucas.stackitem.model.StatusUsuario;
import com.lucas.stackitem.model.Usuario;
import com.lucas.stackitem.model.Venda;
import com.lucas.stackitem.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private VendaService vendaService;

    private Venda venda;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setSobrenome("Silva");
        usuario.setEmail("joao.silva@email.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(new Perfil(1L, "ADMINISTRADOR"));
        usuario.setStatus(StatusUsuario.ATIVO);

        venda = new Venda();
        venda.setId(1L);
        venda.setValorTotal(new BigDecimal("100.50"));
        venda.setValorLucro(new BigDecimal("25.00"));
        venda.setUsuario(usuario);
    }

    @Test
    void testCreate() {
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);

        Venda resultado = vendaService.create(venda);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("100.50"), resultado.getValorTotal());
        verify(vendaRepository, times(1)).save(venda);
    }

    @Test
    void testFindById() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        Optional<Venda> resultado = vendaService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(new BigDecimal("100.50"), resultado.get().getValorTotal());
        verify(vendaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(vendaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Venda> resultado = vendaService.findById(99L);

        assertFalse(resultado.isPresent());
        verify(vendaRepository, times(1)).findById(99L);
    }

    @Test
    void testFindAll() {
        List<Venda> vendas = Arrays.asList(venda, new Venda());
        when(vendaRepository.findAll()).thenReturn(vendas);

        List<Venda> resultado = vendaService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(vendaRepository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(vendaRepository.findAll()).thenReturn(Arrays.asList());

        List<Venda> resultado = vendaService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(vendaRepository, times(1)).findAll();
    }

    @Test
    void testFindByUsuarioId() {
        List<Venda> vendas = Arrays.asList(venda);
        when(vendaRepository.findByUsuarioId(1L)).thenReturn(vendas);

        List<Venda> resultado = vendaService.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(vendaRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    void testFindByUsuarioIdEmpty() {
        when(vendaRepository.findByUsuarioId(99L)).thenReturn(Arrays.asList());

        List<Venda> resultado = vendaService.findByUsuarioId(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(vendaRepository, times(1)).findByUsuarioId(99L);
    }

    @Test
    void testUpdate() {
        Venda vendaAtualizada = new Venda();
        vendaAtualizada.setValorTotal(new BigDecimal("200.00"));

        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaAtualizada);

        Venda resultado = vendaService.update(1L, vendaAtualizada);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("200.00"), resultado.getValorTotal());
        verify(vendaRepository, times(1)).findById(1L);
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    void testUpdateNotFound() {
        Venda vendaAtualizada = new Venda();
        when(vendaRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vendaService.update(99L, vendaAtualizada)
        );

        assertEquals("Venda não encontrada", exception.getMessage());
        verify(vendaRepository, times(1)).findById(99L);
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    void testDelete() {
        when(vendaRepository.existsById(1L)).thenReturn(true);

        vendaService.delete(1L);

        verify(vendaRepository, times(1)).existsById(1L);
        verify(vendaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(vendaRepository.existsById(99L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> vendaService.delete(99L)
        );

        assertEquals("Venda não encontrada", exception.getMessage());
        verify(vendaRepository, times(1)).existsById(99L);
        verify(vendaRepository, never()).deleteById(anyLong());
    }
}