package com.lucas.stackitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.stackitem.config.TestSecurityConfig;
import com.lucas.stackitem.model.Perfil;
import com.lucas.stackitem.service.PerfilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PerfilController.class)
@Import(TestSecurityConfig.class)
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfilService perfilService;

    @Autowired
    private ObjectMapper objectMapper;

    private Perfil perfil;

    @BeforeEach
    void setUp() {
        perfil = new Perfil(1L, "ADMINISTRADOR");
    }

    @Test
    void testCreate() throws Exception {
        when(perfilService.create(any(Perfil.class))).thenReturn(perfil);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(perfil)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ADMINISTRADOR"));

        verify(perfilService, times(1)).create(any(Perfil.class));
    }

    @Test
    void testFindById() throws Exception {
        when(perfilService.findById(1L)).thenReturn(Optional.of(perfil));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ADMINISTRADOR"));

        verify(perfilService, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        when(perfilService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());

        verify(perfilService, times(1)).findById(99L);
    }

    @Test
    void testFindAll() throws Exception {
        when(perfilService.findAll()).thenReturn(Arrays.asList(perfil, new Perfil(2L, "VENDEDOR")));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(perfilService, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() throws Exception {
        when(perfilService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(perfilService, times(1)).findAll();
    }

    @Test
    void testUpdate() throws Exception {
        Perfil perfilAtualizado = new Perfil(1L, "VENDEDOR");

        when(perfilService.update(eq(1L), any(Perfil.class))).thenReturn(perfilAtualizado);

        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(perfilAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("VENDEDOR"));

        verify(perfilService, times(1)).update(eq(1L), any(Perfil.class));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        Perfil perfilAtualizado = new Perfil(99L, "INEXISTENTE");
        when(perfilService.update(eq(99L), any(Perfil.class)))
                .thenThrow(new IllegalArgumentException("Perfil não encontrado"));

        mockMvc.perform(put("/products/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(perfilAtualizado)))
                .andExpect(status().isNotFound());

        verify(perfilService, times(1)).update(eq(99L), any(Perfil.class));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(perfilService).delete(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(perfilService, times(1)).delete(1L);
    }

    @Test
    void testDeleteNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Perfil não encontrado"))
                .when(perfilService).delete(99L);

        mockMvc.perform(delete("/products/99"))
                .andExpect(status().isNotFound());

        verify(perfilService, times(1)).delete(99L);
    }
}