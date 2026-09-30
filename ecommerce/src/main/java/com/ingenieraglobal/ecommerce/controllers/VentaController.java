package com.ingenieraglobal.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.VentaDTO;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.VentaService;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.ingenieraglobal.ecommerce.dtos.PaginaDTO;
import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;

@RestController
@RequestMapping("api/v1/ventas")
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<ApiResponse<VentaDTO>> crearVenta(
            Authentication authentication) {
        Long usuarioId = Long.parseLong(authentication.getName());

        VentaDTO venta = ventaService.crearVenta(usuarioId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        venta,
                        "Pedido realizado con éxito"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginaDTO<VentaDTO>>> obtenerMisVentas(
            Authentication authentication,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "6") int size,

            @RequestParam(required = false) EstadoVentaEnum estado,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,

            @RequestParam(required = false) BigDecimal precioMin,

            @RequestParam(required = false) BigDecimal precioMax) {

        Long usuarioId = Long.parseLong(authentication.getName());

        PaginaDTO<VentaDTO> ventas = ventaService.obtenerVentasDelUsuario(
                usuarioId,
                page,
                size,
                estado,
                fechaDesde,
                fechaHasta,
                precioMin,
                precioMax);

        return ResponseEntity.ok(
                ApiResponse.success(ventas));
    }
}
