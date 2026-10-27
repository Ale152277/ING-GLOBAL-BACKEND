package com.ingenieraglobal.ecommerce.services;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.ProductoDTO;
import com.ingenieraglobal.ecommerce.exceptions.ValidationException;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.repositories.ProductoRepository;

@Service
@Transactional(readOnly = true)
public class AdminProductoService {
    private final ProductoRepository productoRepository;

    public AdminProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Page<ProductoDTO> filtrarProductos(
            String texto,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Integer stockMin,
            Integer stockMax,
            EstadoEnum estado,
            int page,
            int size) {

        if (page < 1 || size < 1 || size > 100) {
            throw new ValidationException("Paginación inválida");
        }

        if ((precioMin != null && precioMin.signum() < 0)
                || (precioMax != null && precioMax.signum() < 0)
                || (precioMin != null && precioMax != null && precioMin.compareTo(precioMax) > 0)) {
            throw new ValidationException("Rango de precios invalidos");

        }
        if ((stockMin != null && stockMin < 0)
                || (stockMax != null && stockMax < 0)
                || (stockMin != null && stockMax != null
                        && stockMin > stockMax)) {
            throw new ValidationException("Rango de stock inválido");
        }

        String criterio = texto == null || texto.isBlank()
                ? null
                : texto.trim();

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("updatedAt").descending());

        return productoRepository.filtrarParaAdmin(
                criterio, precioMin, precioMax,
                stockMin, stockMax, estado, pageable).map(ProductoDTO::new);
    }

}
