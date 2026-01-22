package com.ingenieraglobal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.repositories.MarcaRepository;
import com.ingenieraglobal.models.enums.EstadoEnum;
import com.ingenieraglobal.dtos.response.ApiResponse;

import java.util.List;


@RestController
@RequestMapping("/api/v1/marcas")
@CrossOrigin("http::/localhost:4200")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> obtenerTodas(){
        List<?> marcas = marcaRepository.findByEstado(EstadoEnum.ACTIVO).stream().map(m-> new Object(){
            public Long id = m.getId();
            public String nombre = m.getNombre();
            public String logo = m.getLogo();
        }).toList();

        return ResponseEntity.ok(ApiResponse.success(marcas));

    }
    
}
