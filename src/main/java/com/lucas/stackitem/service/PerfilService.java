package com.lucas.stackitem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.lucas.stackitem.model.Perfil;
import com.lucas.stackitem.repository.PerfilRepository;


@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;
       
    public Perfil create(Perfil perfil) {

        return perfilRepository.save(perfil);
    }

    public List<Perfil> listarTodos() {
        return perfilRepository.findAll();
    }
 
    public Optional<Perfil> findById(Long id) {
        return perfilRepository.findById(id);
    }

    public List<Perfil> findAll() {
        return perfilRepository.findAll();
    }
    
    public Perfil update(Long id, Perfil perfilAtualizado) {
      Perfil existingPerfil =  findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado"));
        
        existingPerfil.setNome(perfilAtualizado.getNome());
        return perfilRepository.save(existingPerfil);
    }
  
    public void delete(Long id) {
     if (!perfilRepository.existsById(id)) {
            throw new IllegalArgumentException("Perfil não encontrado");
        }
        perfilRepository.deleteById(id);
    }

}
