package com.ingenieraglobal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.ingenieraglobal.models.Usuario;
import com.ingenieraglobal.models.enums.EstadoEnum;
import com.ingenieraglobal.models.enums.RolEnum;
import com.ingenieraglobal.repositories.UsuarioRepository;
import com.ingenieraglobal.models.Usuario;
import com.ingenieraglobal.dtos.request.RegistroUsuarioRequest;
import com.ingenieraglobal.dtos.response.ApiResponse;

import java.util.Optional;

@Service
@Transactional

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ApiResponse<String> registrar(RegistroUsuarioRequest request){
        if(usuarioRepository.existsByEmail(request.getEmail())){
            return ApiResponse.error("El email ya está registrado");
        }

        Usuario usuario = new Usuario(
            request.getNombreCompleto(),
            request.getEmail(),
            passwordEncoder.encode((request.getContraseña()))

        );

        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setRol(RolEnum.USER);
        usuario.setEstado(EstadoEnum.ACTIVO);

        usuarioRepository.save(usuario);

        return ApiResponse.success("Usuario registrado exitosamente");
    }

    public Optional <Usuario> obtenerPorEmail(String email){
        return usuarioRepository.findByEmail(email);

    }

    public Optional <Usuario> obtenerPorId(Long id){
        return usuarioRepository.findById(id).filter (u-> u.getEstado() == EstadoEnum.ACTIVO);
    }
    
}
