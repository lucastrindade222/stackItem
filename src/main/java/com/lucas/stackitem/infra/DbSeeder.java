package com.lucas.stackitem.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.lucas.stackitem.model.Perfil;
import com.lucas.stackitem.model.PerfilUsuario;
import com.lucas.stackitem.model.StatusUsuario;
import com.lucas.stackitem.model.Usuario;
import com.lucas.stackitem.repository.PerfilRepository;
import com.lucas.stackitem.repository.UsuarioRepository;
import com.lucas.stackitem.service.PerfilService;
import com.lucas.stackitem.service.UsuarioService;

@Configuration
public class DbSeeder implements CommandLineRunner {
 
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilRepository perfilRepository;
    
    @Autowired
    private PerfilService perfilService;

    @Value("${spring.profiles.active}")
    private String profilesActive;


    @Override
    public void run(String... args) throws Exception {

        if ("dev".equalsIgnoreCase(profilesActive)) {

            if(perfilRepository.count() == 0) {
                addProfiles();
            }

            if(usuarioRepository.count() == 0) {
                addAdminUser();
            }
        }
    }

    private void addProfiles() {
        Perfil perfilAdmin = new Perfil();
                perfilAdmin.setId(1L);
                perfilAdmin.setNome("ADMINISTRADOR");
                perfilService.create(perfilAdmin);
    }

    private void addAdminUser() {


        Perfil perfilAdmin = perfilService.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Perfil ADMINISTRADOR não encontrado"));

          Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Lucas");
        usuario.setSobrenome("dev java");
        usuario.setEmail("lucas.dev.java@email.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(perfilAdmin);
        usuario.setStatus(StatusUsuario.ATIVO);

        try{
            usuarioService.create(usuario);
        
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
