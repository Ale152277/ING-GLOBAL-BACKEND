package com.ingenieraglobal.ecommerce.controllers;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieraglobal.ecommerce.dtos.PagoDTO;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.PagoService;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/{pagoId}")
    public ResponseEntity<ApiResponse<PagoDTO>> obtenerPago(
            Authentication authentication,
            @PathVariable Long pagoId) {

        Long usuarioId = Long.parseLong(authentication.getName());
        PagoDTO pago = pagoService.obtenerPagoDelUsuario(pagoId, usuarioId);

        return ResponseEntity.ok(ApiResponse.success(pago));
    }

    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<ApiResponse<List<PagoDTO>>> obtenerPagosDeVenta(
            Authentication authentication,
            @PathVariable Long ventaId) {

        Long usuarioId = Long.parseLong(authentication.getName());
        List<PagoDTO> pagos = pagoService.obtenerPagosDeVenta(ventaId, usuarioId);

        return ResponseEntity.ok(ApiResponse.success(pagos));
    }

    @GetMapping("/{pagoId}/comprobante")
    public ResponseEntity<Resource> obtenerComprobante(
            Authentication authentication,
            @PathVariable Long pagoId) {

        Long usuarioId = Long.parseLong(authentication.getName());
        Resource recurso = pagoService.obtenerComprobanteDelUsuario(pagoId, usuarioId);

        MediaType tipoContenido = MediaTypeFactory
                .getMediaType(recurso)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(tipoContenido)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    @PostMapping(value = "/{pagoId}/comprobante", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PagoDTO>> registrarComprobante(
            Authentication authentication,
            @PathVariable Long pagoId,
            @RequestPart("archivo") MultipartFile archivo) {

        Long usuarioId = Long.parseLong(authentication.getName());

        PagoDTO pago = pagoService.registrarComprobantePagoManual(
                pagoId,
                usuarioId,
                archivo);

        return ResponseEntity.ok(
                ApiResponse.success(pago, "Comprobante registrado correctamente"));
    }
}
