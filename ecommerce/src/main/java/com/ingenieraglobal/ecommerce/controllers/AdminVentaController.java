package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ingenieraglobal.ecommerce.dtos.PaginaDTO;
import com.ingenieraglobal.ecommerce.dtos.admin.AdminVentaResumenDTO;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;
import com.ingenieraglobal.ecommerce.services.AdminVentaService;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.ingenieraglobal.ecommerce.dtos.admin.ActualizarEstadoVentaDTO;
import com.ingenieraglobal.ecommerce.dtos.AdminVentaDetalleDTO;

@RestController
@RequestMapping("/api/v1/admin/ventas")
public class AdminVentaController {

        private final AdminVentaService adminVentaService;

        public AdminVentaController(AdminVentaService adminVentaService) {
                this.adminVentaService = adminVentaService;

        }

        @GetMapping
        public ResponseEntity<ApiResponse<PaginaDTO<AdminVentaResumenDTO>>> obtenerVentas(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) EstadoVentaEnum estado

        ) {
                PaginaDTO<AdminVentaResumenDTO> ventas = adminVentaService.obtenerVentas(
                                page,
                                size,
                                estado);

                return ResponseEntity.ok(
                                ApiResponse.success(ventas));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<AdminVentaDetalleDTO>> obtenerVentaPorId(
                        @PathVariable Long id) {

                AdminVentaDetalleDTO venta = adminVentaService.obtenerVentaPorId(id);

                return ResponseEntity.ok(
                                ApiResponse.success(venta));
        }

        @PatchMapping("/{id}/estado")
        public ResponseEntity<ApiResponse<AdminVentaResumenDTO>> actualizarEstado(
                        @PathVariable Long id,
                        @RequestBody ActualizarEstadoVentaDTO request) {

                AdminVentaResumenDTO venta = adminVentaService.actualizarEstado(
                                id,
                                request.getEstado());

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                venta,
                                                "Estado del pedido actualizado correctamente"));
        }

}
