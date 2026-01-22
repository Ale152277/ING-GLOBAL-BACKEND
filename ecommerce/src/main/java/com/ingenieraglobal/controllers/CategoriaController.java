package com.ingenieraglobal.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.services.CategoriaService;
import com.ingenieraglobal.dtos.CategoriaDTO;
import com.ingenieraglobal.dtos.response.ApiResponse;

import java.util.List;


@RestController
@RequestMapping("api/v1/categorias")
@CrossOrigin (origins = "http://localhost:4200")

public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaDTO>>> obtenerTodas(){
        List<CategoriaDTO> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(ApiResponse.success(categorias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> obtenerPorId(@PathVariable Long id){
        return categoriaService.obtenerPorId(id).map(categoria -> ResponseEntity.ok(ApiResponse.success(categoria))).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> obtenerPorSlug(@PathVariable String slug){
        return categoriaService.obtenerPorSlug(slug).map(categoria -> ResponseEntity.ok(ApiResponse.success(categoria))).orElse(ResponseEntity.notFound().build());
    }

    
}
