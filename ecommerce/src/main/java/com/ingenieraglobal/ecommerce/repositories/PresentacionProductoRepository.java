package com.ingenieraglobal.ecommerce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.ecommerce.models.PresentacionProducto;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import java.util.List;

@Repository
public interface PresentacionProductoRepository extends JpaRepository<PresentacionProducto, Long> {
    List<PresentacionProducto> findByProductoIdAndEstado(Long productoId, EstadoEnum estado);

}
