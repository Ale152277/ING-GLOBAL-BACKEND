package com.ingenieraglobal.ecommerce.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.ecommerce.models.Venta;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByUsuarioIdOrderByFechaVentaDesc(Long usuarioId);

    boolean existsByCarritoId(Long carritoId);

    long countByEstado(EstadoVentaEnum estado);

    List<Venta> findTop5ByOrderByFechaVentaDesc();

    List<Venta> findByFechaVentaGreaterThanEqualOrderByFechaVentaAsc(
            LocalDateTime fechaDesde);

    Page<Venta> findAllByOrderByFechaVentaDesc(Pageable pageable);

    Page<Venta> findByEstadoOrderByFechaVentaDesc(
            EstadoVentaEnum estado,
            Pageable pageable);

    @Query("""
                SELECT v
                FROM Venta v
                WHERE v.usuario.id = :usuarioId
                  AND (:estado IS NULL OR v.estado = :estado)
                  AND (:fechaDesde IS NULL OR v.fechaVenta >= :fechaDesde)
                  AND (:fechaHasta IS NULL OR v.fechaVenta < :fechaHasta)
                  AND (:precioMin IS NULL OR v.total >= :precioMin)
                  AND (:precioMax IS NULL OR v.total <= :precioMax)
                ORDER BY v.fechaVenta DESC
            """)
    Page<Venta> buscarVentasDelUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("estado") EstadoVentaEnum estado,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            Pageable pageable);

}
