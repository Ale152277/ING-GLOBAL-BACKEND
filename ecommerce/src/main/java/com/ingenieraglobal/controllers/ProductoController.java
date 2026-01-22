package com.ingenieraglobal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.services.ProductoService;
import com.ingenieraglobal.dtos.response.ApiResponse;
import com.ingenieraglobal.dtos.response.PageResponse;
import com.ingenieraglobal.dtos.ProductoDTO;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:4200")

public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> listar(
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) Long marcaId,
        @RequestParam(defaultValue = "0") BigDecimal precioMin,
        @RequestParam(defaultValue = "99999") BigDecimal precioMax,
        @RequestParam(defaultValue = "false") boolean soloStock,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "12") int size,
        @RequestParam(required = false) String ordenar
    ){

        Page<ProductoDTO> result = productoService.filtrar(categoriaId, marcaId, precioMin, precioMax, soloStock, page, size, ordenar);
        PageResponse<ProductoDTO> pageResponse = new PageResponse<>(result);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    
    
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtener(@PathVariable Long id){
        return productoService.obtenerPorId(id).map(producto -> ResponseEntity.ok(ApiResponse.success(producto))).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> buscar(
        @RequestParam String q,
        @RequestParam (defaultValue = "1") int page,
        @RequestParam (defaultValue = "12") int size
    ){
        Page<ProductoDTO> result = productoService.buscar(q, page, size);
        PageResponse<ProductoDTO> pageResponse = new PageResponse<>(result);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @GetMapping("/etiqueta/{etiqueta}")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> obtenerPorEitqueta(
        @PathVariable String eitqueta,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "12") int size
    ){
        Page<ProductoDTO> result = productoService.obtenerPorEtiqueta(eitqueta, page, size);
        PageResponse<ProductoDTO> pageResponse = new PageResponse<>(result);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @GetMapping("/ofertas")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> obtenerOfertas(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "12") int size
    ){
        Page<ProductoDTO> result = productoService.obtenerConDescuento(page, size);
        PageResponse<ProductoDTO> pagerResponse = new PageResponse<>(result);
        return ResponseEntity.ok(ApiResponse.success(pagerResponse));
    }








    
}
