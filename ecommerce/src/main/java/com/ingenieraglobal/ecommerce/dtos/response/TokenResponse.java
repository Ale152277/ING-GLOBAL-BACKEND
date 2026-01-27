package com.ingenieraglobal.ecommerce.dtos.response;

import com.ingenieraglobal.ecommerce.dtos.UsuarioDTO;

        //ESTA CLASE ES UN DTO DE RESPUESTA
/*
    Es una forma del JSON que el backend devuelve al cliente
    usado normalmente en /login o /auth/login
    {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipo": "bearer",
    "expiresIn": 86400000,
    "usuario": {
        "id": 1,
        "email": "user@mail.com",
        "nombre": "Juan"
    }
}

*/
public class TokenResponse {
    private String token;
    private String tipo = "bearer";
    private Long expiresIn; // milisegundos
    private UsuarioDTO usuario;

    public TokenResponse() {

    }

    public TokenResponse(String token, Long expiresIn, UsuarioDTO usuario) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setGetExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

}
