package com.ingenieraglobal.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenieraglobal.ecommerce.models.Direccion;

@Repository
public interface DireccionRepository
        extends JpaRepository<Direccion, Long> {

    List<Direccion> findByUsuario_IdOrderByPredeterminadaDescIdDesc(
            Long usuarioId);

    Optional<Direccion> findByIdAndUsuario_Id(
            Long id, Long usuarioId);

    Optional<Direccion> findByUsuario_IdAndPredeterminadaTrue(
            Long usuarioId);
}