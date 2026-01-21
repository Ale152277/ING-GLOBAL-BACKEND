package com.ingenieraglobal.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ingenieraglobal.models.Usuario;
import com.ingenieraglobal.models.enums.EstadoEnum;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario>findByEstado(EstadoEnum estado);
    
}
