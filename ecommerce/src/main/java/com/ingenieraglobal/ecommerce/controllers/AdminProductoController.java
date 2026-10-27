
package com.ingenieraglobal.ecommerce.controllers;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieraglobal.ecommerce.dtos.ProductoDTO;
import com.ingenieraglobal.ecommerce.dtos.request.CrearProductoRequest;
import com.ingenieraglobal.ecommerce.dtos.request.EditarProductoRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.dtos.response.PageResponse;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.services.AdminProductoService;
import com.ingenieraglobal.ecommerce.services.ImagenService;
import com.ingenieraglobal.ecommerce.services.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/productos")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductoController {

    private final AdminProductoService adminProductoService;
    private final ProductoService productoService;
    private final ImagenService imagenService;

    public AdminProductoController(
            AdminProductoService adminProductoService,
            ProductoService productoService,
            ImagenService imagenService) {

        this.adminProductoService = adminProductoService;
        this.productoService = productoService;
        this.imagenService = imagenService;
    }

    @GetMapping("/admin/todos")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> listarTodos(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) Integer stockMin,
            @RequestParam(required = false) Integer stockMax,
            @RequestParam(required = false) EstadoEnum estado,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<ProductoDTO> productos = adminProductoService.filtrarProductos(
                texto, precioMin, precioMax,
                stockMin, stockMax, estado, page, size
        );

        return ResponseEntity.ok(
                ApiResponse.success(new PageResponse<>(productos))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(
            @Valid @RequestBody CrearProductoRequest request) {

        ProductoDTO producto = productoService.crearProducto(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(producto, "PRODUCTO CREADO EXITOSAMENTE"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> editarProducto(
            @PathVariable Long id,
            @Valid @RequestBody EditarProductoRequest request) {

        request.setId(id);

        ProductoDTO producto = productoService.editarProducto(request);

        return ResponseEntity.ok(
                ApiResponse.success(producto, "PRODUCTO ACTUALIZADO EXITOSAMENTE")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(
            @PathVariable Long id) {

        productoService.eliminarProducto(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "PRODUCTO ELIMINADO EXITOSAMENTE")
        );
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<ProductoDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {

        ProductoDTO producto = productoService.cambiarEstado(id, estado);

        return ResponseEntity.ok(
                ApiResponse.success(producto, "Estado actualizado")
        );
    }

    @PostMapping("/upload-imagen")
    public ResponseEntity<ApiResponse<String>> subirImagen(
            @RequestParam("archivo") MultipartFile archivo) {

        String url = imagenService.subirImagen(archivo);

        return ResponseEntity.ok(
                ApiResponse.success(url, "Imagen subida con exito")
        );
    }
}
