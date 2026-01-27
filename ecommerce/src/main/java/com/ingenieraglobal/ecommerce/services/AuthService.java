package com.ingenieraglobal.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ingenieraglobal.ecommerce.dtos.UsuarioDTO;
import com.ingenieraglobal.ecommerce.dtos.request.LoginRequest;
import com.ingenieraglobal.ecommerce.dtos.request.RegistroUsuarioRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.dtos.response.TokenResponse;
import com.ingenieraglobal.ecommerce.models.Usuario;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.models.enums.RolEnum;
import com.ingenieraglobal.ecommerce.repositories.UsuarioRepository;
import com.ingenieraglobal.ecommerce.utils.JwtUtils;
import com.ingenieraglobal.ecommerce.utils.PasswordUtils;

//clase para centralizar la logica de autenticacion y registro
//quien puede iniciar sesion registrarse
//a quien se le entrega un token
//a quien se rechaza

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private JwtUtils jwtUtils;

    public ApiResponse<String> registrar(RegistroUsuarioRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ApiResponse.error(("el email ya está registrado"));
        }

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setEmail(request.getEmail());
        usuario.setContraseñaHash(passwordUtils.encriptarContraseña(request.getContraseña()));
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setRol(RolEnum.USER);
        usuario.setEstado(EstadoEnum.ACTIVO);

        usuarioRepository.save(usuario);
        return ApiResponse.success(null, "Usuario registrado exitosamente");

    }

    public ApiResponse<TokenResponse> login(LoginRequest request) {
        var usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);

        // validar que el email exista
        if (usuario == null) {
            return ApiResponse.error("Email o contraseña incorrectos");
        }

        // validar que la contraseña exista

        if (!passwordUtils.verificarContraseña(request.getContraseña(), usuario.getContraseñaHash())) {
            return ApiResponse.error("Email o contraseña incorrectos");
        }

        // validar que la cuenta esté activa
        if (usuario.getEstado() != EstadoEnum.ACTIVO) {
            return ApiResponse.error("La cuenta está inactiva");
        }

        // generar token
        /*
         * el token contiene
         * claim => Id
         * subject => email
         * expiration => 24h
         */
        String token = jwtUtils.generarToken(usuario.getId(), usuario.getEmail());

        // convertir usuario a DTO
        /*
         * nunca devolver entidades, siempre DTOs
         */
        UsuarioDTO usuarioDTO = convertirADTO(usuario);

        // crear una respuesta con token
        TokenResponse tokenResponse = new TokenResponse(
                token,
                86400000L,
                usuarioDTO

        );

        return ApiResponse.success(tokenResponse, "Login exitoso");

    }

    /*
     * “Tengo un Usuario completo (entidad),
     * pero SOLO quiero enviar al frontend
     * lo que es seguro y útil.”
     */
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setRol(usuario.getRol());
        dto.setEstado(usuario.getEstado());
        return dto;

    }

}
