package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.request.LoginRequest;
import com.ingenieraglobal.ecommerce.dtos.request.RegistroUsuarioRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.UsuarioService;

import jakarta.validation.*;

@RestController
@RequestMapping("/api/v1/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<String>> registrar(
            @Valid @RequestBody RegistroUsuarioRequest request) {
        ApiResponse<String> response = usuarioService.registrar(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @Valid @RequestBody LoginRequest request) {
        return usuarioService.obtenerPorEmail(request.getEmail()).map(usuario -> {
            String token = "token_temp_" + usuario.getId();
            return ResponseEntity.ok(ApiResponse.success(token, "Login exitoso"));
        })
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Email o contraseña incorrectos")));
    }

}
