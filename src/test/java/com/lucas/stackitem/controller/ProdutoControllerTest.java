package com.lucas.stackitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.stackitem.config.TestSecurityConfig;
import com.lucas.stackitem.model.Produto;
import com.lucas.stackitem.service.ProdutoService;
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

@WebMvcTest(ProdutoController.class)
@Import(TestSecurityConfig.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição do produto");
        produto.setPercentualLucro(new BigDecimal("25.50"));
    }

    @Test
    void testCreate() throws Exception {
        when(produtoService.create(any(Produto.class))).thenReturn(produto);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"));

        verify(produtoService, times(1)).create(any(Produto.class));
    }

    @Test
    void testFindById() throws Exception {
        when(produtoService.findById(1L)).thenReturn(Optional.of(produto));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"));

        verify(produtoService, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        when(produtoService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());

        verify(produtoService, times(1)).findById(99L);
    }

    @Test
    void testFindAll() throws Exception {
        when(produtoService.findAll()).thenReturn(Arrays.asList(produto, new Produto()));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(produtoService, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() throws Exception {
        when(produtoService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(produtoService, times(1)).findAll();
    }

    @Test
    void testUpdate() throws Exception {
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNome("Produto Atualizado");

        when(produtoService.update(eq(1L), any(Produto.class))).thenReturn(produtoAtualizado);

        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"));

        verify(produtoService, times(1)).update(eq(1L), any(Produto.class));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        Produto produtoAtualizado = new Produto();
        when(produtoService.update(eq(99L), any(Produto.class)))
                .thenThrow(new IllegalArgumentException("Produto não encontrado"));

        mockMvc.perform(put("/products/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isNotFound());

        verify(produtoService, times(1)).update(eq(99L), any(Produto.class));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(produtoService).delete(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(produtoService, times(1)).delete(1L);
    }

    @Test
    void testDeleteNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Produto não encontrado"))
                .when(produtoService).delete(99L);

        mockMvc.perform(delete("/products/99"))
                .andExpect(status().isNotFound());

        verify(produtoService, times(1)).delete(99L);
    }
}