package com.ingenieraglobal.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.models.Producto;
import com.ingenieraglobal.models.enums.EstadoEnum;
import java.math.BigDecimal;
import java.util.Optional;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{

    Optional <Producto> findBySku(String sku);
    Page<Producto> findByCategoriaIdAndEstado(Long categoriaId, EstadoEnum estado, Pageable pageable);

    @Query("""
            SELECT p FROM Producto p
            WHERE p.estado = :estado
            AND(:categoriaId IS NULL OR p.categoria.id = :categoriaID)
            AND(:marcaId IS NULL OR p.marca.id = :marcaId)
            AND(p.precio BETWEEN :precioMin AND :precioMax)
            """;)
    
    
}
