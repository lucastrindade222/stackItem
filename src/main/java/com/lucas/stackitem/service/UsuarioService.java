package com.lucas.stackitem.service;

import com.lucas.stackitem.model.Usuario;
import com.lucas.stackitem.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario create(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario existingUsuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        if (!existingUsuario.getEmail().equals(usuario.getEmail()) && 
            usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}