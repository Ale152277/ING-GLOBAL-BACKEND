package com.ingenieraglobal.ecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ingenieraglobal.ecommerce.models.Producto;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findBySku(String sku);

    Page<Producto> findByCategoriaIdAndEstado(Long categoriaId, EstadoEnum estado, Pageable pageable);

    @Query("""
            SELECT p FROM Producto p
            WHERE p.estado = :estado
            AND(:categoriaId IS NULL OR p.categoria.id = :categoriaId)
            AND(:marcaId IS NULL OR p.marca.id = :marcaId)
            AND(p.precio BETWEEN :precioMin AND :precioMax)
            AND(:soloStock = false OR p.stock >0)
            """)

    Page<Producto> filtrar(
            @Param("estado") EstadoEnum estado,
            @Param("categoriaId") Long categoriaId,
            @Param("marcaId") Long marcaId,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            @Param("soloStock") Boolean soloStock,
            Pageable pageable);

    @Query("""
                 SELECT p FROM Producto p
                 WHERE p.estado = :estado
                 AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
                 OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')))
            """)

    Page<Producto> buscarPorTermino(
            @Param("termino") String termino,
            @Param("estado") EstadoEnum estado,
            Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.etiqueta = :etiqueta AND p.estado = :estado")
    Page<Producto> findPorEtiqueta(
            @Param("etiqueta") String etiqueta,
            @Param("estado") EstadoEnum estado,
            Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.descuento > 0 AND p.estado = :estado ORDER BY p.descuento DESC")
    Page<Producto> findConDescuento(@Param("estado") EstadoEnum estado, Pageable pageable);
    
    @Query("SELECT p FROM Producto p WHERE p.estado = :estado ORDER BY p.rating DESC NULLS LAST")
    Page<Producto> findMejorCalificados(@Param("estado") EstadoEnum estado, Pageable pageable);
    
    @Query("SELECT p FROM Producto p WHERE p.estado = :estado ORDER BY p.createdAt DESC")
    Page<Producto> findMasRecientes(@Param("estado") EstadoEnum estado, Pageable pageable);
    

}


//:"propiedad" es una parametro de consulta o parametro dinammico que se reemplazará con valor real en tiempo de ejecucion