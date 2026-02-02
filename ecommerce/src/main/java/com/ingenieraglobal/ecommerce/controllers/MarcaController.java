package com.ingenieraglobal.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.MarcaDTO;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.repositories.MarcaRepository;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/marcas")
@CrossOrigin("http::/localhost:4200")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
public ResponseEntity<ApiResponse<List<MarcaDTO>>> obtenerTodas() {
    List<MarcaDTO> marcas = marcaRepository.findAll()
        .stream()
        .map(MarcaDTO::new)
        .collect(Collectors.toList());
    return ResponseEntity.ok(ApiResponse.success(marcas));
}
    
}
