package com.ingenieraglobal.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.models.Categoria;
import com.ingenieraglobal.models.enums.EstadoEnum;
import java.util.List;
import java.util.Optional;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    Optional<Categoria>findBySlug(String slug);
    List<Categoria>findbyCategorias(EstadoEnum estado);
}
