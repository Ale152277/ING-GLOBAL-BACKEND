package com.ingenieraglobal.ecommerce.controllers;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingenieraglobal.ecommerce.dtos.AdminPagoDTO;
import com.ingenieraglobal.ecommerce.dtos.RechazarPagoRequestDTO;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.AdminPagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/pagos")
public class AdminPagoController {

    private final AdminPagoService adminPagoService;

    public AdminPagoController(AdminPagoService adminPagoService) {
        this.adminPagoService = adminPagoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminPagoDTO>>> obtenerPagos() {
        return ResponseEntity.ok(
                ApiResponse.success(adminPagoService.obtenerPagos()));
    }

    @GetMapping("/revision")
    public ResponseEntity<ApiResponse<List<AdminPagoDTO>>> obtenerPagosEnRevision() {
        return ResponseEntity.ok(
                ApiResponse.success(adminPagoService.obtenerPagosEnRevision()));
    }

    @PatchMapping("/{pagoId}/aprobar")
    public ResponseEntity<ApiResponse<AdminPagoDTO>> aprobarPago(
            @PathVariable Long pagoId) {

        AdminPagoDTO pago = adminPagoService.aprobarPagoManual(pagoId);

        return ResponseEntity.ok(
                ApiResponse.success(pago, "Pago aprobado correctamente"));
    }

    @PatchMapping("/{pagoId}/rechazar")
    public ResponseEntity<ApiResponse<AdminPagoDTO>> rechazarPago(
            @PathVariable Long pagoId,
            @Valid @RequestBody RechazarPagoRequestDTO request) {

        AdminPagoDTO pago = adminPagoService.rechazarPagoManual(
                pagoId,
                request.getMotivo());

        return ResponseEntity.ok(
                ApiResponse.success(pago, "Pago rechazado correctamente"));
    }

    @GetMapping("/{pagoId}/comprobante")
    public ResponseEntity<Resource> obtenerComprobante(
            @PathVariable Long pagoId) {

        Resource recurso = adminPagoService.obtenerComprobante(pagoId);

        MediaType tipoContenido = MediaTypeFactory
                .getMediaType(recurso)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(tipoContenido)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="" + recurso.getFilename() + """)
                .body(recurso);
    }
}
