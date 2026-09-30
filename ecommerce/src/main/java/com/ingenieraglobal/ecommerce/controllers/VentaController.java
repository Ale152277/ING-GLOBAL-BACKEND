package com.ingenieraglobal.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.VentaDTO;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.services.VentaService;



@RestController 
@RequestMapping ("api/v1/ventas")
@CrossOrigin (origins = "http://localhost:4200")
public class VentaController {

    @Autowired 
    private VentaService ventaService;

    @PostMapping 
    public ResponseEntity<ApiResponse<VentaDTO>> crearVenta(
        Authentication authentication
    ){
        Long usuarioId = Long.parseLong(authentication.getName());

        VentaDTO venta = ventaService.crearVenta(usuarioId);

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.success(
            venta,
            "Pedido realizado con éxito"
        ));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<VentaDTO>>> obtenerMisVentas(
        Authentication authentication){
            Long usuarioId = Long.parseLong(authentication.getName());

            List<VentaDTO> ventas = ventaService.obtenerVentasDelUsuario(usuarioId);

            return ResponseEntity.ok(ApiResponse.success(ventas));

        }    
}
