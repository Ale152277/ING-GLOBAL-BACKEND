package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.request.LoginRequest;
import com.ingenieraglobal.ecommerce.dtos.request.RegistroUsuarioRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.dtos.response.TokenResponse;
import com.ingenieraglobal.ecommerce.services.AuthService;
import com.ingenieraglobal.ecommerce.services.UsuarioService;

import jakarta.validation.*;

@RestController
@RequestMapping("/api/v1/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthService authService;

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

    @PostMapping("/registro/admin")
    public ResponseEntity<ApiResponse<String>> crearAdmin(
            @Valid @RequestBody RegistroUsuarioRequest request) {
        ApiResponse<String> response = usuarioService.crearAdmin(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        ApiResponse<TokenResponse> response = authService.login(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
