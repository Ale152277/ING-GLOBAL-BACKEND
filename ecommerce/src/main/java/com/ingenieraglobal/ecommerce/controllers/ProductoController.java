
package com.ingenieraglobal.ecommerce.controllers;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.ProductoDTO;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.dtos.response.PageResponse;
import com.ingenieraglobal.ecommerce.services.ProductoService;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> buscar(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<ProductoDTO> result = productoService.buscar(q, page, size);

        return ResponseEntity.ok(
                ApiResponse.success(new PageResponse<>(result))
        );
    }

    @GetMapping("/ofertas")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> obtenerOfertas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<ProductoDTO> result =
                productoService.obtenerConDescuento(page, size);

        return ResponseEntity.ok(
                ApiResponse.success(new PageResponse<>(result))
        );
    }

    @GetMapping("/etiqueta/{etiqueta}")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> obtenerPorEtiqueta(
            @PathVariable String etiqueta,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<ProductoDTO> result =
                productoService.obtenerPorEtiqueta(etiqueta, page, size);

        return ResponseEntity.ok(
                ApiResponse.success(new PageResponse<>(result))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> listar(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long marcaId,
            @RequestParam(defaultValue = "0") BigDecimal precioMin,
            @RequestParam(defaultValue = "99999") BigDecimal precioMax,
            @RequestParam(defaultValue = "false") boolean soloStock,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String ordenar,
            @RequestParam(required = false) String estado) {

        Page<ProductoDTO> result = productoService.filtrar(
                categoriaId, marcaId, precioMin, precioMax,
                soloStock, page, size, ordenar
        );

        return ResponseEntity.ok(
                ApiResponse.success(new PageResponse<>(result))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtener(
            @PathVariable Long id) {

        return productoService.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(ApiResponse.success(producto)))
                .orElse(ResponseEntity.notFound().build());
    }
}
