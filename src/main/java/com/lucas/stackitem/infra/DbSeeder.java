package com.lucas.stackitem.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.lucas.stackitem.model.PerfilUsuario;
import com.lucas.stackitem.model.StatusUsuario;
import com.lucas.stackitem.model.Usuario;
import com.lucas.stackitem.service.UsuarioService;


@Configuration
public class DbSeeder implements CommandLineRunner {
 
    @Autowired
    private UsuarioService usuarioService;

    @Value("${spring.profiles.active}")
    private String profilesActive;


    @Override
    public void run(String... args) throws Exception {

        if ("dev".equalsIgnoreCase(profilesActive)) {
            Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Lucas");
        usuario.setSobrenome("dev java");
        usuario.setEmail("lucas.dev.java@email.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(PerfilUsuario.ADMINISTRADOR);
        usuario.setStatus(StatusUsuario.ATIVO);

        try{
            usuarioService.create(usuario);
        
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
}
