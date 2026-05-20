package com.lucas.stackitem.service;

import com.lucas.stackitem.dto.LoginRequest;
import com.lucas.stackitem.dto.LoginResponse;
import com.lucas.stackitem.model.Perfil;
import com.lucas.stackitem.model.StatusUsuario;
import com.lucas.stackitem.model.Usuario;
import com.lucas.stackitem.repository.UsuarioRepository;
import com.lucas.stackitem.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UsuarioService usuarioService;

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
    }

    @Test
    void testCreate() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.create(usuario);

        assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        assertEquals("joao.silva@email.com", resultado.getEmail());
        // Verifica que a senha foi criptografada antes de salvar
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(usuarioRepository, times(1)).existsByEmail(usuario.getEmail());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testCreateDuplicateEmail() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.create(usuario)
        );

        assertEquals("Email já cadastrado", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuario.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testFindById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("João", resultado.get().getNome());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findById(99L);

        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test
    void testFindByEmail() {
        when(usuarioRepository.findByEmail("joao.silva@email.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.findByEmail("joao.silva@email.com");

        assertTrue(resultado.isPresent());
        assertEquals("João", resultado.get().getNome());
        verify(usuarioRepository, times(1)).findByEmail("joao.silva@email.com");
    }

    @Test
    void testFindByEmailNotFound() {
        when(usuarioRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findByEmail("inexistente@email.com");

        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findByEmail("inexistente@email.com");
    }

    @Test
    void testFindAll() {
        List<Usuario> usuarios = Arrays.asList(usuario, new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList());

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("João Atualizado");
        usuarioAtualizado.setEmail("joao.atualizado@email.com");
        usuarioAtualizado.setSenha("novaSenha");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("joao.atualizado@email.com")).thenReturn(false);
        when(passwordEncoder.encode("novaSenha")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        Usuario resultado = usuarioService.update(1L, usuarioAtualizado);

        assertNotNull(resultado);
        assertEquals("João Atualizado", resultado.getNome());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).existsByEmail("joao.atualizado@email.com");
        verify(passwordEncoder, times(1)).encode("novaSenha");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testUpdateNotFound() {
        Usuario usuarioAtualizado = new Usuario();
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.update(99L, usuarioAtualizado)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99L);
        verify(usuarioRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testUpdateDuplicateEmail() {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setEmail("email.existente@email.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("email.existente@email.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.update(1L, usuarioAtualizado)
        );

        assertEquals("Email já cadastrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).existsByEmail("email.existente@email.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testUpdateSameUsuarioSameEmail() {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setEmail("joao.silva@email.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        Usuario resultado = usuarioService.update(1L, usuarioAtualizado);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).existsByEmail(anyString());
        // Mantém a senha existente pois nenhuma nova foi fornecida
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testDelete() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.delete(1L);

        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.delete(99L)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).existsById(99L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    // Testes de Login

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("joao.silva@email.com");
        loginRequest.setSenha("senha123");

        when(usuarioRepository.findByEmail("joao.silva@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", usuario.getSenha())).thenReturn(true);
        when(jwtTokenProvider.generateToken("joao.silva@email.com")).thenReturn("tokenJWT");

        LoginResponse response = usuarioService.login(loginRequest);

        assertNotNull(response);
        assertEquals("tokenJWT", response.getToken());
        assertEquals(usuario, response.getUsuario());
        verify(usuarioRepository, times(1)).findByEmail("joao.silva@email.com");
        verify(passwordEncoder, times(1)).matches("senha123", usuario.getSenha());
        verify(jwtTokenProvider, times(1)).generateToken("joao.silva@email.com");
    }

    @Test
    void testLoginEmailNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("naoexiste@email.com");
        loginRequest.setSenha("senha123");

        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.login(loginRequest)
        );

        assertEquals("Email ou senha inválidos", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail("naoexiste@email.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString());
    }

    @Test
    void testLoginWrongPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("joao.silva@email.com");
        loginRequest.setSenha("senhaErrada");

        when(usuarioRepository.findByEmail("joao.silva@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", usuario.getSenha())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.login(loginRequest)
        );

        assertEquals("Email ou senha inválidos", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail("joao.silva@email.com");
        verify(passwordEncoder, times(1)).matches("senhaErrada", usuario.getSenha());
        verify(jwtTokenProvider, never()).generateToken(anyString());
    }

    @Test
    void testUploadImage() throws IOException {
        MultipartFile arquivo = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", new byte[]{0x01, 0x02, 0x03});

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.uploadImage(1L, arquivo);

        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, usuario.getImagemPerfil());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testUploadImageUsuarioNotFound() {
        MultipartFile arquivo = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", new byte[0]);
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.uploadImage(99L, arquivo)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testCreateEncryptsPassword() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("$2a$10$senhaCriptografada");
        
        Usuario usuarioComSenhaCriptografada = new Usuario();
        usuarioComSenhaCriptografada.setId(1L);
        usuarioComSenhaCriptografada.setNome("João");
        usuarioComSenhaCriptografada.setSenha("$2a$10$senhaCriptografada");
        
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioComSenhaCriptografada);

        Usuario resultado = usuarioService.create(usuario);

        assertNotNull(resultado);
        // Verifica que a senha foi realmente criptografada (diferente da original)
        assertNotEquals("senha123", resultado.getSenha());
        assertEquals("$2a$10$senhaCriptografada", resultado.getSenha());
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}