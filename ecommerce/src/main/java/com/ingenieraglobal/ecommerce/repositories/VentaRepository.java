package com.ingenieraglobal.ecommerce.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.ecommerce.models.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>{

    List<Venta> findByUsuarioIdOrderByFechaVentaDesc(Long usuarioId);

    boolean existsByCarritoId(Long carritoId);
    
}
