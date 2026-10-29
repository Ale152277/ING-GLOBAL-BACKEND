package com.ingenieraglobal.ecommerce.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ingenieraglobal.ecommerce.models.Provincia;

public interface ProvinciaRepository
        extends JpaRepository<Provincia, String> {

    List<Provincia> findByDepartamento_CodigoOrderByNombreAsc(
            String codigoDepartamento);
}