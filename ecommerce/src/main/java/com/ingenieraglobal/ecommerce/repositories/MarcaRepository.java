package com.ingenieraglobal.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenieraglobal.ecommerce.models.Marca;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;

import java.util.List;
import java.util.Optional;



@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Optional<Marca> findByNombre(String nombre);
    List<Marca> findByEstado(EstadoEnum estado);
}
