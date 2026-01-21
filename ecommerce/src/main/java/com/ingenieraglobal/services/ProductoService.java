package com.ingenieraglobal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ingenieraglobal.models.Producto;
import com.ingenieraglobal.models.enums.EstadoEnum;
import com.ingenieraglobal.repositories.ProductoRepository;
import com.ingenieraglobal.dtos.ProductoDTO;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional(readOnly = true)

public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Optional<ProductoDTO> obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .filter(p -> p.getEstado() == EstadoEnum.ACTIVO)
                .map(ProductoDTO::new);
    }

    public Page<ProductoDTO> filtrar(
            Long categoriaId,
            Long marcaId,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Boolean soloStock,
            int page,
            int size,
            String ordenar) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                crearOrdenamiento(ordenar));

        return productoRepository.filtrar(
                EstadoEnum.ACTIVO,
                categoriaId,
                marcaId,
                precioMin != null ? precioMin : BigDecimal.ZERO,
                precioMax != null ? precioMax : new BigDecimal("999999"),
                soloStock != null ? soloStock : false,
                pageable).map(ProductoDTO::new);
    }

    public Page<ProductoDTO> buscar(String termino, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productoRepository.buscarPorTermino(termino, EstadoEnum.ACTIVO, pageable)
                .map(ProductoDTO::new);
    }

    public Page<ProductoDTO> obtenerPorEtiqueta(String etiqueta, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productoRepository.findPorEtiqueta(etiqueta, EstadoEnum.ACTIVO, pageable)
                .map(ProductoDTO::new);
    }

    public Page<ProductoDTO> obtenerConDescuento(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productoRepository.findConDescuento(EstadoEnum.ACTIVO, pageable)
                .map(ProductoDTO::new);
    }

    private Sort crearOrdenamiento(String ordenar) {
        if (ordenar == null) {
            return Sort.by("nombre").ascending();
        }

        return switch (ordenar) {
            case "precio-asc" -> Sort.by("precio").ascending();
            case "precio-desc" -> Sort.by("precio").descending();
            case "rating" -> Sort.by("rating").descending();
            case "nuevo" -> Sort.by("createdAt").descending();
            default -> Sort.by("nombre").ascending();
        };
    }
}