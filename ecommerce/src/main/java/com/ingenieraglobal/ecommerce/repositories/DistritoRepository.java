package com.ingenieraglobal.ecommerce.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ingenieraglobal.ecommerce.models.Distrito;

public interface DistritoRepository
        extends JpaRepository<Distrito, String> {

    List<Distrito> findByProvincia_CodigoOrderByNombreAsc(
            String codigoProvincia);
}