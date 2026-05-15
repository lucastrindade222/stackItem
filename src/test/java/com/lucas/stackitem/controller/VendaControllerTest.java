package com.lucas.stackitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.stackitem.config.TestSecurityConfig;
import com.lucas.stackitem.model.PerfilUsuario;
import com.lucas.stackitem.model.StatusUsuario;
import com.lucas.stackitem.model.Usuario;
import com.lucas.stackitem.model.Venda;
import com.lucas.stackitem.service.VendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendaController.class)
@Import(TestSecurityConfig.class)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendaService vendaService;

    @Autowired
    private ObjectMapper objectMapper;

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
    }

    @Test
    void testCreate() throws Exception {
        when(vendaService.create(any(Venda.class))).thenReturn(venda);

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venda)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valorTotal").value(100.50));

        verify(vendaService, times(1)).create(any(Venda.class));
    }

    @Test
    void testFindById() throws Exception {
        when(vendaService.findById(1L)).thenReturn(Optional.of(venda));

        mockMvc.perform(get("/sales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valorTotal").value(100.50));

        verify(vendaService, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        when(vendaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/sales/99"))
                .andExpect(status().isNotFound());

        verify(vendaService, times(1)).findById(99L);
    }

    @Test
    void testFindAll() throws Exception {
        when(vendaService.findAll()).thenReturn(Arrays.asList(venda, new Venda()));

        mockMvc.perform(get("/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(vendaService, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() throws Exception {
        when(vendaService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(vendaService, times(1)).findAll();
    }

    @Test
    void testFindByUsuarioId() throws Exception {
        when(vendaService.findByUsuarioId(1L)).thenReturn(Arrays.asList(venda));

        mockMvc.perform(get("/sales/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(vendaService, times(1)).findByUsuarioId(1L);
    }

    @Test
    void testFindByUsuarioIdEmpty() throws Exception {
        when(vendaService.findByUsuarioId(99L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/sales/user/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(vendaService, times(1)).findByUsuarioId(99L);
    }

    @Test
    void testUpdate() throws Exception {
        Venda vendaAtualizada = new Venda();
        vendaAtualizada.setId(1L);
        vendaAtualizada.setValorTotal(new BigDecimal("200.00"));

        when(vendaService.update(eq(1L), any(Venda.class))).thenReturn(vendaAtualizada);

        mockMvc.perform(put("/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendaAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valorTotal").value(200.00));

        verify(vendaService, times(1)).update(eq(1L), any(Venda.class));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        Venda vendaAtualizada = new Venda();
        when(vendaService.update(eq(99L), any(Venda.class)))
                .thenThrow(new IllegalArgumentException("Venda não encontrada"));

        mockMvc.perform(put("/sales/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendaAtualizada)))
                .andExpect(status().isNotFound());

        verify(vendaService, times(1)).update(eq(99L), any(Venda.class));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(vendaService).delete(1L);

        mockMvc.perform(delete("/sales/1"))
                .andExpect(status().isNoContent());

        verify(vendaService, times(1)).delete(1L);
    }

    @Test
    void testDeleteNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Venda não encontrada"))
                .when(vendaService).delete(99L);

        mockMvc.perform(delete("/sales/99"))
                .andExpect(status().isNotFound());

        verify(vendaService, times(1)).delete(99L);
    }
}