package com.ingenieraglobal.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ingenieraglobal.ecommerce.models.Pago;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByVenta_IdOrderByFechaCreacionDesc(Long ventaId);

    boolean existsByVenta_IdAndEstadoPago(Long ventaId, EstadoPagoEnum estadoPago);

    Optional<Pago> findByIdAndVenta_Usuario_Id(Long pagoId, Long usuarioId);

    List<Pago> findByVenta_IdAndVenta_Usuario_IdOrderByFechaCreacionDesc(Long ventaId, Long usuarioId);

    List<Pago> findByEstadoPagoOrderByFechaComprobanteAsc(EstadoPagoEnum estadoPago);
}
