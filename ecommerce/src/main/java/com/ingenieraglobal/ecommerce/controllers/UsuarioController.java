package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.request.RegistroUsuarioRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.UsuarioService;
import org.springframework.security.core.Authentication;
import com.ingenieraglobal.ecommerce.dtos.UsuarioDTO;
import com.ingenieraglobal.ecommerce.dtos.request.ActualizarPerfilRequest;
import org.springframework.security.core.Authentication;
import com.ingenieraglobal.ecommerce.dtos.request.CambiarContrasenaRequest;

import jakarta.validation.*;

@RestController
@RequestMapping("/api/v1/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarUsuario(@PathVariable Long id) {
        ApiResponse<String> response = usuarioService.eliminarUsuario(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerMiPerfil(
            Authentication authentication) {

        Long usuarioId = Long.parseLong(authentication.getName());
        UsuarioDTO perfil = usuarioService.obtenerMiPerfil(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(perfil));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizarMiPerfil(
            Authentication authentication,
            @Valid @RequestBody ActualizarPerfilRequest request) {

        Long usuarioId = Long.parseLong(authentication.getName());
        UsuarioDTO perfil = usuarioService.actualizarMiPerfil(usuarioId, request);

        return ResponseEntity.ok(ApiResponse.success(perfil, "Perfil actualizado correctamente"));
    }

    @PutMapping("/me/contrasena")
    public ResponseEntity<ApiResponse<Void>> cambiarContrasena(
            Authentication authentication,
            @Valid @RequestBody CambiarContrasenaRequest request) {
        Long usuarioId = Long.parseLong(authentication.getName());

        usuarioService.cambiarContrasena(usuarioId, request);

        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Contraseña actualizada correctamente"));
    }

}
