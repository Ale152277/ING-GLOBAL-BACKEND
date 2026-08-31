package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.ProductoDTO;
import com.ingenieraglobal.ecommerce.dtos.request.CrearProductoRequest;
import com.ingenieraglobal.ecommerce.dtos.request.EditarProductoRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.dtos.response.PageResponse;
import com.ingenieraglobal.ecommerce.services.ProductoService;
import com.ingenieraglobal.ecommerce.services.ImagenService;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:4200")

public class ProductoController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ImagenService imagenService;

    

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

    @GetMapping("/ofertas")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> obtenerOfertas(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "12") int size
    ){
        Page<ProductoDTO> result = productoService.obtenerConDescuento(page, size);
        PageResponse<ProductoDTO> pagerResponse = new PageResponse<>(result);
        return ResponseEntity.ok(ApiResponse.success(pagerResponse));
    }


    @GetMapping("/etiqueta/{etiqueta}")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> obtenerPorEtiqueta(
        @PathVariable String etiqueta,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "12") int size
    ){
        Page<ProductoDTO> result = productoService.obtenerPorEtiqueta(etiqueta, page, size);
        PageResponse<ProductoDTO> pageResponse = new PageResponse<>(result);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
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
        @RequestParam(required = false) String estado 

    ){

        Page<ProductoDTO> result = productoService.filtrar(categoriaId, marcaId, precioMin, precioMax, soloStock, page, size, ordenar);
        PageResponse<ProductoDTO> pageResponse = new PageResponse<>(result);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    
    
    }
    

    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtener(@PathVariable Long id){
        return productoService.obtenerPorId(id).map(producto -> ResponseEntity.ok(ApiResponse.success(producto))).orElse(ResponseEntity.notFound().build());
    }




    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(
        @Valid @RequestBody CrearProductoRequest request
    ){
        ProductoDTO productoCreado = productoService.crearProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(productoCreado, "PRODUCTO CREADO EXITOSAMENTE"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductoDTO>> editarProducto(
        @PathVariable Long id,
        @Valid @RequestBody EditarProductoRequest request
    ){
        request.setId(id);
        ProductoDTO productoEditado = productoService.editarProducto(request);
        return ResponseEntity.ok(ApiResponse.success(productoEditado, "PRODUCTO ACTUALIZADO EXITOSAMENTE"));
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(
        @PathVariable Long id
    ){
        productoService.eliminarProducto(id);
        return ResponseEntity.ok(ApiResponse.success(null, "PRODUCTO ELIMINADO EXITOSAMENTE"));
    }

        @PatchMapping("/{id}/estado")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<ProductoDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado
        ){
              ProductoDTO productoActualizado = productoService.cambiarEstado(id, estado);
    return ResponseEntity.ok(ApiResponse.success(productoActualizado, "Estado actualizado"));

        }



    @GetMapping("/admin/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<ProductoDTO>>> listarTodos(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "12") int size
    ){
        Page<ProductoDTO> result = productoService.obtenerTodosParaAdmin(page, size);
        PageResponse<ProductoDTO> pageResponse = new PageResponse<>(result);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));

    }


    @PostMapping("/upload-imagen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> subirImagen(
        @RequestParam("archivo") MultipartFile archivo

    ){
        String url = imagenService.subirImagen(archivo);
        return ResponseEntity.ok(ApiResponse.success(url, "Imagen subida con exito"));
    }
    









    
}
