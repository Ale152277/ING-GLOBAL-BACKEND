package com.ingenieraglobal.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.models.DetalleCarrito;
import java.util.List;
import java.util.Optional;


@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long>{
    List<DetalleCarrito> findByCarritoId(Long carritoId);
    Optional<DetalleCarrito>findByCarritoIdAndProductoId(Long carritoId, Long productoId);
    void deleteByCarritoId(Long carritoId);
    
}
