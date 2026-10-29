package com.ingenieraglobal.ecommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.DireccionDTO;
import com.ingenieraglobal.ecommerce.dtos.request.DireccionRequest;
import com.ingenieraglobal.ecommerce.exceptions.RecursoNoEncontradoException;
import com.ingenieraglobal.ecommerce.models.Direccion;
import com.ingenieraglobal.ecommerce.models.Usuario;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.repositories.DireccionRepository;
import com.ingenieraglobal.ecommerce.repositories.UsuarioRepository;

@Service
@Transactional
public class DireccionService {

    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository;

    public DireccionService(
            DireccionRepository direccionRepository,
            UsuarioRepository usuarioRepository) {
        this.direccionRepository = direccionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<DireccionDTO> listar(Long usuarioId) {
        return direccionRepository
                .findByUsuario_IdOrderByPredeterminadaDescIdDesc(usuarioId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public DireccionDTO crear(Long usuarioId, DireccionRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .filter(u -> u.getEstado() == EstadoEnum.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        boolean primera = direccionRepository
                .findByUsuario_IdOrderByPredeterminadaDescIdDesc(usuarioId)
                .isEmpty();

        Direccion direccion = new Direccion();
        direccion.setUsuario(usuario);
        asignarDatos(direccion, request);

        if (primera || Boolean.TRUE.equals(request.predeterminada())) {
            quitarPredeterminada(usuarioId);
            direccion.setPredeterminada(true);
        }

        return convertirDTO(direccionRepository.save(direccion));
    }

    public DireccionDTO actualizar(
            Long usuarioId, Long direccionId, DireccionRequest request) {

        Direccion direccion = obtenerPropia(usuarioId, direccionId);
        asignarDatos(direccion, request);

        if (Boolean.TRUE.equals(request.predeterminada())) {
            quitarPredeterminada(usuarioId);
            direccion.setPredeterminada(true);
        }

        return convertirDTO(direccionRepository.save(direccion));
    }

    public void eliminar(Long usuarioId, Long direccionId) {
        Direccion direccion = obtenerPropia(usuarioId, direccionId);

        if (direccion.isPredeterminada()) {
            direccionRepository
                    .findByUsuario_IdOrderByPredeterminadaDescIdDesc(usuarioId)
                    .stream()
                    .filter(d -> !d.getId().equals(direccionId))
                    .findFirst()
                    .ifPresent(d -> d.setPredeterminada(true));
        }

        direccionRepository.delete(direccion);
    }

    public DireccionDTO establecerPredeterminada(
            Long usuarioId, Long direccionId) {

        Direccion direccion = obtenerPropia(usuarioId, direccionId);

        quitarPredeterminada(usuarioId);
        direccion.setPredeterminada(true);

        return convertirDTO(direccionRepository.save(direccion));
    }

    private Direccion obtenerPropia(Long usuarioId, Long direccionId) {
        return direccionRepository.findByIdAndUsuario_Id(direccionId, usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Dirección no encontrada"));
    }

    private void quitarPredeterminada(Long usuarioId) {
        direccionRepository
                .findByUsuario_IdAndPredeterminadaTrue(usuarioId)
                .ifPresent(d -> d.setPredeterminada(false));
    }

    private void asignarDatos(Direccion direccion, DireccionRequest request) {
        direccion.setAlias(request.alias().trim());
        direccion.setNombreReceptor(request.nombreReceptor().trim());
        direccion.setTelefono(request.telefono().trim());
        direccion.setDepartamento(request.departamento().trim());
        direccion.setProvincia(request.provincia().trim());
        direccion.setDistrito(request.distrito().trim());
        direccion.setDireccion(request.direccion().trim());

        direccion.setReferencia(limpiarOpcional(request.referencia()));
        direccion.setCodigoPostal(limpiarOpcional(request.codigoPostal()));
    }

    private String limpiarOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private DireccionDTO convertirDTO(Direccion d) {
        return new DireccionDTO(
                d.getId(),
                d.getAlias(),
                d.getNombreReceptor(),
                d.getTelefono(),
                d.getDepartamento(),
                d.getProvincia(),
                d.getDistrito(),
                d.getDireccion(),
                d.getReferencia(),
                d.getCodigoPostal(),
                d.isPredeterminada());
    }
}