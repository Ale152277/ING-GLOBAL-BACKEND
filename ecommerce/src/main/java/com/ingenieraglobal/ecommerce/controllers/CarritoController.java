package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{usuarioId}")
    //wrapper de varias capas
    //ResponseEntity es una case spring que envuelve una respuesta HTTP completa
    /*
        El cuerpo de la respuesta (el dato que se quiere devolver, en este caso el carrito de compras).

        El código de estado HTTP (por ejemplo, 200 OK si la operación fue exitosa).

        Los encabezados HTTP (información adicional sobre la respuesta, como tipo de contenido, cookies, etc.).
    */
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

    @DeleteMapping("/{carritoId}/vaciar")
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
