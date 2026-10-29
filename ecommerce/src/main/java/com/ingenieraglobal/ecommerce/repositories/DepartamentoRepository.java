package com.ingenieraglobal.ecommerce.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ingenieraglobal.ecommerce.models.Departamento;

public interface DepartamentoRepository
        extends JpaRepository<Departamento, String> {

    List<Departamento> findAllByOrderByNombreAsc();
}