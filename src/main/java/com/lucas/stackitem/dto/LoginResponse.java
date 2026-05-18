package com.lucas.stackitem.dto;

import com.lucas.stackitem.model.Usuario;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Usuario usuario;

    public LoginResponse(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }
}