package com.ingenieraglobal.ecommerce.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.DireccionDTO;
import com.ingenieraglobal.ecommerce.dtos.request.DireccionRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.DireccionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/direcciones")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DireccionDTO>>> listar(
            Authentication authentication) {

        Long usuarioId = obtenerUsuarioId(authentication);

        return ResponseEntity.ok(
                ApiResponse.success(direccionService.listar(usuarioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DireccionDTO>> crear(
            Authentication authentication,
            @Valid @RequestBody DireccionRequest request) {

        Long usuarioId = obtenerUsuarioId(authentication);

        DireccionDTO direccion = direccionService.crear(usuarioId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        direccion, "Dirección registrada correctamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DireccionDTO>> actualizar(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody DireccionRequest request) {

        Long usuarioId = obtenerUsuarioId(authentication);

        DireccionDTO direccion = direccionService.actualizar(
                usuarioId, id, request);

        return ResponseEntity.ok(
                ApiResponse.success(direccion, "Dirección actualizada correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            Authentication authentication,
            @PathVariable Long id) {

        Long usuarioId = obtenerUsuarioId(authentication);

        direccionService.eliminar(usuarioId, id);

        return ResponseEntity.ok(
                ApiResponse.<Void>success(null, "Dirección eliminada correctamente"));
    }

    @PutMapping("/{id}/predeterminada")
    public ResponseEntity<ApiResponse<DireccionDTO>> establecerPredeterminada(
            Authentication authentication,
            @PathVariable Long id) {

        Long usuarioId = obtenerUsuarioId(authentication);

        DireccionDTO direccion = direccionService.establecerPredeterminada(
                usuarioId, id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        direccion, "Dirección predeterminada actualizada"));
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}