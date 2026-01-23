package com.ingenieraglobal.ecommerce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenieraglobal.ecommerce.models.Categoria;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    Optional<Categoria>findBySlug(String slug);
    List<Categoria>findByEstadoOrderByOrden(EstadoEnum estado);
}
