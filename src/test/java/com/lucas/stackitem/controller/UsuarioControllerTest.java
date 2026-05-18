package com.lucas.stackitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.stackitem.config.TestSecurityConfig;
import com.lucas.stackitem.dto.LoginRequest;
import com.lucas.stackitem.dto.LoginResponse;
import com.lucas.stackitem.model.PerfilUsuario;
import com.lucas.stackitem.model.StatusUsuario;
import com.lucas.stackitem.model.Usuario;
import com.lucas.stackitem.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
@Import(TestSecurityConfig.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

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
    }

    @Test
    void testCreate() throws Exception {
        when(usuarioService.create(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"));

        verify(usuarioService, times(1)).create(any(Usuario.class));
    }

    @Test
    void testFindById() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"));

        verify(usuarioService, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).findById(99L);
    }

    @Test
    void testFindAll() throws Exception {
        when(usuarioService.findAll()).thenReturn(Arrays.asList(usuario, new Usuario()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() throws Exception {
        when(usuarioService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void testUpdate() throws Exception {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(1L);
        usuarioAtualizado.setNome("João Atualizado");

        when(usuarioService.update(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Atualizado"));

        verify(usuarioService, times(1)).update(eq(1L), any(Usuario.class));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        Usuario usuarioAtualizado = new Usuario();
        when(usuarioService.update(eq(99L), any(Usuario.class)))
                .thenThrow(new IllegalArgumentException("Usuário não encontrado"));

        mockMvc.perform(put("/users/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioAtualizado)))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).update(eq(99L), any(Usuario.class));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(usuarioService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).delete(1L);
    }

    @Test
    void testDeleteNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Usuário não encontrado"))
                .when(usuarioService).delete(99L);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).delete(99L);
    }

    // Testes de Login

    @Test
    void testLogin() throws Exception {
        LoginResponse loginResponse = new LoginResponse("tokenJWT", usuario);
        when(usuarioService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("joao.silva@email.com");
        loginRequest.setSenha("senha123");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tokenJWT"))
                .andExpect(jsonPath("$.usuario.id").value(1))
                .andExpect(jsonPath("$.usuario.nome").value("João"))
                .andExpect(jsonPath("$.usuario.email").value("joao.silva@email.com"));

        verify(usuarioService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        when(usuarioService.login(any(LoginRequest.class)))
                .thenThrow(new IllegalArgumentException("Email ou senha inválidos"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("joao.silva@email.com");
        loginRequest.setSenha("senhaErrada");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(usuarioService, times(1)).login(any(LoginRequest.class));
    }
}
