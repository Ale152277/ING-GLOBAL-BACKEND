package com.ingenieraglobal.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.request.RegistroUsuarioRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.models.Usuario;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.models.enums.RolEnum;
import com.ingenieraglobal.ecommerce.repositories.UsuarioRepository;
import com.ingenieraglobal.ecommerce.dtos.UsuarioDTO;
import com.ingenieraglobal.ecommerce.dtos.request.ActualizarPerfilRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import com.ingenieraglobal.ecommerce.dtos.request.CambiarContrasenaRequest;

import java.util.Optional;

@Service
@Transactional

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ApiResponse<String> crearAdmin(RegistroUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("El email ya está registrado");
        }

        Usuario usuario = new Usuario(
                request.getNombreCompleto(),
                request.getEmail(),
                passwordEncoder.encode(request.getContraseña()));

        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setRol(RolEnum.ADMIN);
        usuario.setEstado(EstadoEnum.ACTIVO);
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);

        return ApiResponse.success("Admin creado exitosamente");
    }

    @Transactional
    public ApiResponse<String> eliminarUsuario(Long id) {
        Optional<Usuario> opt = usuarioRepository.findById(id);

        if (opt.isEmpty()) {
            return ApiResponse.error("Usuario no encontrado");
        }

        opt.get().setEstado(EstadoEnum.INACTIVO);
        usuarioRepository.save(opt.get());
        return ApiResponse.success("Usuario eliminado correctamente");

    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);

    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).filter(u -> u.getEstado() == EstadoEnum.ACTIVO);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerMiPerfil(Long usuarioId) {
        Usuario usuario = buscarUsuarioActivo(usuarioId);
        return convertirDTO(usuario);
    }

    public UsuarioDTO actualizarMiPerfil(Long usuarioId, ActualizarPerfilRequest request) {

        Usuario usuario = buscarUsuarioActivo(usuarioId);
        usuario.setNombreCompleto(request.getNombreCompleto().trim());
        String telefono = request.getTelefono();

        usuario.setTelefono(telefono == null | telefono.isBlank() ? null : telefono.trim());

        usuario.setFechaActualizacion(java.time.LocalDateTime.now());
        usuarioRepository.save(usuario);
        return convertirDTO(usuario);

    }

    private Usuario buscarUsuarioActivo(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .filter(u -> u.getEstado() == EstadoEnum.ACTIVO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private UsuarioDTO convertirDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO(
                usuario.getId(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.getEstado());

        dto.setFechaRegistro(usuario.getFechaRegistro());

        return dto;
    }

    public void cambiarContrasena(Long usuarioId, CambiarContrasenaRequest request) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .filter(u -> u.getEstado() == EstadoEnum.ACTIVO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(
                request.getContrasenaActual(),
                usuario.getContraseñaHash())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta");
        }

        if (passwordEncoder.matches(
                request.getContrasenaNueva(),
                usuario.getContraseñaHash())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La contraseña nueva debe ser diferente");
        }

        if (request.getContrasenaNueva()
                .getBytes(StandardCharsets.UTF_8).length > 72) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La contraseña es demasiado larga");
        }

        usuario.setContraseñaHash(passwordEncoder.encode(request.getContrasenaNueva()));

        usuario.setVersionToken(usuario.getVersionToken() + 1);
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

}
