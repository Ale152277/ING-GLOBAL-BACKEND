package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.CarritoDTO;
import com.ingenieraglobal.ecommerce.dtos.request.AgregarAlCarritoRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.CarritoService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/v1/carrito")
@CrossOrigin(origins = "http://localhost:4200")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<ApiResponse<CarritoDTO>> obtener 
    (Authentication authentication){
        Long usuarioId = Long.parseLong(authentication.getName());
        CarritoDTO carrito = carritoService.obtenerCarritoActivo(usuarioId).orElse(null);
        return ResponseEntity.ok(ApiResponse.success(carrito));
    }

    @PostMapping("/agregar")
    public ResponseEntity<ApiResponse<CarritoDTO>> agregarProducto(
        Authentication authentication,
        @Valid @RequestBody AgregarAlCarritoRequest request
    ){
        Long usuarioId = Long.parseLong(authentication.getName());
        CarritoDTO carrito = carritoService.agregarProducto(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(carrito));

    }

    @DeleteMapping("/detalle/{detalleId}")
    public ResponseEntity<Void> eliminarProducto(Authentication authentication ,@PathVariable Long detalleId, @RequestParam Long carritoId)
    {
        Long usuarioId = Long.parseLong(authentication.getName());
        carritoService.eliminarProducto(usuarioId ,carritoId, detalleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{carritoId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(Authentication authentication ,@PathVariable Long carritoId){

        Long usuarioId = Long.parseLong(authentication.getName());
        carritoService.vaciarCarrito(usuarioId ,carritoId);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{carritoId}/enviar-whatsapp")
    public ResponseEntity<ApiResponse<CarritoDTO>> enviarWhatsapp(Authentication authentication ,@PathVariable Long carritoId){
        Long usuarioId = Long.parseLong(authentication.getName());
        CarritoDTO carrito = carritoService.enviarAWhatsapp(usuarioId, carritoId);
        return ResponseEntity.ok(ApiResponse.success(carrito, "Carrito enviado correctamente"));
    }

    @PutMapping("detalle/{detalleId}")
    public ResponseEntity<ApiResponse<CarritoDTO>> actualizarCantidad(
        Authentication authentication,
        @PathVariable Long detalleId,
        @RequestParam Integer cantidad,
        @RequestParam Long carritoId
    ){
        Long usuarioId = Long.parseLong(authentication.getName());
        CarritoDTO carrito = carritoService.actualizarCantidad(usuarioId ,carritoId, detalleId, cantidad);
        return ResponseEntity.ok(ApiResponse.success(carrito));
    }


    
}
