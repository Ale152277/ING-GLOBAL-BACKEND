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
    public ApiResponse<String>eliminarUsuario(Long id){
        Optional<Usuario>opt = usuarioRepository.findById(id);

        if(opt.isEmpty()){
            return ApiResponse.error("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
        return ApiResponse.success("Usuario eliminado correctamente");
    }


    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);

    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).filter(u -> u.getEstado() == EstadoEnum.ACTIVO);
    }



    

}
