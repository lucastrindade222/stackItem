package com.lucas.stackitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.stackitem.config.TestSecurityConfig;
import com.lucas.stackitem.model.Tag;
import com.lucas.stackitem.service.TagService;
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

@WebMvcTest(TagController.class)
@Import(TestSecurityConfig.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setId(1L);
        tag.setNome("Tag Teste");
        tag.setDescricao("Descrição da tag");
    }

    @Test
    void testCreate() throws Exception {
        when(tagService.create(any(Tag.class))).thenReturn(tag);

        mockMvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Tag Teste"));

        verify(tagService, times(1)).create(any(Tag.class));
    }

    @Test
    void testFindById() throws Exception {
        when(tagService.findById(1L)).thenReturn(Optional.of(tag));

        mockMvc.perform(get("/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Tag Teste"));

        verify(tagService, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        when(tagService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tags/99"))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).findById(99L);
    }

    @Test
    void testFindAll() throws Exception {
        when(tagService.findAll()).thenReturn(Arrays.asList(tag, new Tag()));

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(tagService, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() throws Exception {
        when(tagService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(tagService, times(1)).findAll();
    }

    @Test
    void testUpdate() throws Exception {
        Tag tagAtualizada = new Tag();
        tagAtualizada.setId(1L);
        tagAtualizada.setNome("Tag Atualizada");

        when(tagService.update(eq(1L), any(Tag.class))).thenReturn(tagAtualizada);

        mockMvc.perform(put("/tags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Tag Atualizada"));

        verify(tagService, times(1)).update(eq(1L), any(Tag.class));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        Tag tagAtualizada = new Tag();
        when(tagService.update(eq(99L), any(Tag.class)))
                .thenThrow(new IllegalArgumentException("Tag não encontrada"));

        mockMvc.perform(put("/tags/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagAtualizada)))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).update(eq(99L), any(Tag.class));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(tagService).delete(1L);

        mockMvc.perform(delete("/tags/1"))
                .andExpect(status().isNoContent());

        verify(tagService, times(1)).delete(1L);
    }

    @Test
    void testDeleteNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Tag não encontrada"))
                .when(tagService).delete(99L);

        mockMvc.perform(delete("/tags/99"))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).delete(99L);
    }
}