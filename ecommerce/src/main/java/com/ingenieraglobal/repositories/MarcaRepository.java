package com.ingenieraglobal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.models.Marca;
import com.ingenieraglobal.models.enums.EstadoEnum;
import java.util.List;
import java.util.Optional;



@Repository
public interface MarcaRepository extends JpaRepository {
    Optional<Marca> findByNombre(String nombre);
    List<Marca> findByEstado(EstadoEnum estado);
}
