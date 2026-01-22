package com.ingenieraglobal.controllers;

import org.springframework.http.HttpStatus;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.ingenieraglobal.services.CarritoService;
import com.ingenieraglobal.dtos.CarritoDTO;
import com.ingenieraglobal.dtos.response.ApiResponse;
import com.ingenieraglobal.dtos.request.AgregarAlCarritoRequest;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<ApiResponse<CarritoDTO>> obtener (@PathVariable Long usuarioId){
        CarritoDTO carrito = carritoService.obtenerCarritoActivo(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(carrito));
    }

    @PostMapping("/{usuarioId}/agregar")
    public ResponseEntity<ApiResponse<CarritoDTO>> agregarPrdoucto(
        @PathVariable Long usuarioId,
        @Valid @RequestBody AgregarAlCarritoRequest request
    ){
        CarritoDTO carrito = carritoService.agregarProducto(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(carrito));

    }

    @DeleteMapping("/detalle/{detalleId}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long detalleId, @RequestParam Long carritoId)
    {
        carritoService.eliminarProducto(carritoId, detalleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{carritoId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long carritoId){
        carritoService.vaciarCarrito(carritoId);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{carritoId}/enviar-whatsapp")
    public ResponseEntity<ApiResponse<CarritoDTO>> enviarWhatsapp(@PathVariable Long carritoId){
        CarritoDTO carrito = carritoService.enviarAWhatsapp(carritoId);
        return ResponseEntity.ok(ApiResponse.success(carrito, "Carrito enviado correctamente"));
    }


    
}
