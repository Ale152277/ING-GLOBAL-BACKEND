package com.ingenieraglobal.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.models.Carrito;
import com.ingenieraglobal.models.enums.EstadoCarritoEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.estado = :estado")
    Optional<Carrito> findCarritoActivoByUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("estado") EstadoCarritoEnum estado);

    List<Carrito> findByUsuarioId(Long usuarioId);

    Page<Carrito> findByEstado(EstadoCarritoEnum estado, Pageable pageable);

    @Query("""
                SELECT c FROM Carrito c
                WHERE c.estado = :estado
                AND c.fechaEnvioWhatsapp BETWEEN :fechaInicio AND :fechaFin
            """)
    List<Carrito> findCarritosEnviadosPorFecha(
            @Param("estado") EstadoCarritoEnum estado,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}
