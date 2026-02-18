package com.ingenieraglobal.ecommerce.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.CategoriaDTO;
//import com.ingenieraglobal.ecommerce.models.Categoria;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.repositories.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<CategoriaDTO> obtenerTodas() {
        return categoriaRepository.findByEstadoOrderByOrden(EstadoEnum.ACTIVO)
            .stream()
            .map(CategoriaDTO::new)
            .toList();
    }
    
    public Optional<CategoriaDTO> obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
            .filter(c -> c.getEstado() == EstadoEnum.ACTIVO)
            .map(CategoriaDTO::new);
    }
    
    public Optional<CategoriaDTO> obtenerPorSlug(String slug) {
        return categoriaRepository.findBySlug(slug)
            .filter(c -> c.getEstado() == EstadoEnum.ACTIVO)
            .map(CategoriaDTO::new);
    }
}