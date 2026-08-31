package com.ingenieraglobal.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.ProductoDTO;
import com.ingenieraglobal.ecommerce.dtos.request.CrearProductoRequest;
import com.ingenieraglobal.ecommerce.dtos.request.EditarProductoRequest;
import com.ingenieraglobal.ecommerce.models.Categoria;
import com.ingenieraglobal.ecommerce.models.Marca;
import com.ingenieraglobal.ecommerce.models.Producto;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.repositories.CategoriaRepository;
import com.ingenieraglobal.ecommerce.repositories.MarcaRepository;
import com.ingenieraglobal.ecommerce.repositories.ProductoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)

public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;


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


    @Transactional
    public ProductoDTO crearProducto(CrearProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.getCategoriaId()));

        if (productoRepository.findBySku(request.getSku()).isPresent()) {
            throw new RuntimeException("El SKU ya existe: " + request.getSku());
        }

        Marca marca = null;
        if (request.getMarcaId() != null) {
            marca = marcaRepository.findById(request.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada con id: " + request.getMarcaId()));
        }

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setSku(request.getSku());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setDescripcion(request.getDescripcion());
        producto.setStock(request.getStock() != null ? request.getStock() : 0);
        producto.setDescuento(request.getDescuento() != null ? request.getDescuento() : 0);
        producto.setEtiqueta(request.getEtiqueta());
        producto.setImagen(request.getImagen());
        producto.setRating(request.getRating());
        producto.setEstado(EstadoEnum.ACTIVO);
        producto.setCreatedAt(LocalDateTime.now());
        producto.setUpdatedAt(LocalDateTime.now());

        Producto productoGuardado = productoRepository.save(producto);
        return new ProductoDTO(productoGuardado);

    }

    @Transactional
    public ProductoDTO editarProducto(EditarProductoRequest request) {
        Producto producto = productoRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + request.getId()));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.getCategoriaId()));

        productoRepository.findBySku(request.getSku())
                .ifPresent(p -> {
                    if (!p.getId().equals(request.getId()))
                        throw new RuntimeException("El SKU ya existe: " + request.getSku());
                });

        Marca marca = null;
        if (request.getMarcaId() != null) {
            marca = marcaRepository.findById(request.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada con id: " + request.getMarcaId()));
        }

        producto.setNombre(request.getNombre());
        producto.setSku(request.getSku());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setDescripcion(request.getDescripcion());
        producto.setStock(request.getStock() != null ? request.getStock() : 0);
        producto.setDescuento(request.getDescuento() != null ? request.getDescuento() : 0);
        producto.setEtiqueta(request.getEtiqueta());
        producto.setImagen(request.getImagen());
        producto.setRating(request.getRating());
        producto.setUpdatedAt(LocalDateTime.now());

        Producto productoGuardado = productoRepository.save(producto);
        return new ProductoDTO(productoGuardado);

    }

    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        producto.setEstado(EstadoEnum.INACTIVO);
        producto.setUpdatedAt(LocalDateTime.now());
        productoRepository.save(producto);
    }

    @Transactional
    public ProductoDTO cambiarEstado(Long id, String estado){
        Producto producto = productoRepository.findById((id)).orElseThrow(()-> new RuntimeException("Producto no encontrado"));

        producto.setEstado(EstadoEnum.valueOf(estado));
        producto.setUpdatedAt(LocalDateTime.now());
        Producto productoActualizado = productoRepository.save(producto);
        return new ProductoDTO(productoActualizado);
    }

    
    public Page<ProductoDTO> obtenerTodosParaAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("updatedAt").descending());
        return productoRepository.findAll(pageable).map(ProductoDTO::new);
    }


    

    // --------------------MÉTODO PRIVADO--------------------
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