package com.ingenieraglobal.ecommerce.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

//clase para centralizar la logica de autenticacion y registro
//quien puede iniciar sesion registrarse
//a quien se le entrega un token
//a quien se rechaza

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Emailservice emailservice;

    public ApiResponse<String> registrar(RegistroUsuarioRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ApiResponse.error(("el email ya está registrado"));
        }

        String token = UUID.randomUUID().toString();

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setEmail(request.getEmail());
        usuario.setContraseñaHash(passwordEncoder.encode(request.getContraseña()));
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setRol(RolEnum.USER);
        usuario.setEstado(EstadoEnum.ACTIVO);

        usuario.setEmailVerificado(false);
        usuario.setTokenVerificacion(token);
        usuario.setTokenExpiracion(LocalDateTime.now().plusHours(24));

        usuarioRepository.save(usuario);

        try {
            emailservice.enviarEmailVerificacion(
                    usuario.getEmail(),
                    usuario.getNombreCompleto(),
                    token);
        } catch (Exception e) {
            return ApiResponse.success(null,
                    "Cuenta creada, pero no pudimos enviar el email de verificación. " +
                            "Contacta a soporte si no recibes el correo.");
        }

        return ApiResponse.success(null, "Usuario registrado exitosamente, verifica tu buzon de correo");

    }

    public ApiResponse<TokenResponse> login(LoginRequest request) {
        var usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);

        // validar que el email exista
        if (usuario == null) {
            return ApiResponse.error("Email o contraseña incorrectos");
        }

        // validar que la contraseña exista

        if (!passwordEncoder.matches(request.getContraseña(), usuario.getContraseñaHash())) {
            return ApiResponse.error("Email o contraseña incorrectos");
        }

        if (!usuario.isEmailVerificado()) {
            return ApiResponse.error("Debes verificar tu correo electronico antes de iniciar sesión");
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
        String token = jwtUtils.generarToken(usuario.getId(), usuario.getEmail(), usuario.getRol().name());

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

    public ApiResponse<String> verificarEmail(String token) {
        var opt = usuarioRepository.findByTokenVerificacion(token);

        if (opt.isEmpty()) {
            return ApiResponse.error("El enlace de verificacion no es valido");
        }

        Usuario usuario = opt.get();

        if (LocalDateTime.now().isAfter(usuario.getTokenExpiracion())) {
            return ApiResponse.error("El enlace de verificacion expiró, solicita uno nuevo");
        }

        if (usuario.isEmailVerificado()) {
            return ApiResponse.success("Tu cuenta ya está actualmente verificada");
        }

        usuario.setEmailVerificado(true);
        usuario.setTokenVerificacion(null);
        usuario.setTokenExpiracion(null);

        usuarioRepository.save(usuario);

        return ApiResponse.success(null, "¡Cuenta verificada! Ya puedes iniciar sesión");
    }

    public ApiResponse<String> reenviarVerificacion(String email) {
        var opt = usuarioRepository.findByEmail(email);

        if (opt.isEmpty()) {
            return ApiResponse.error("No existe una cuenta con ese correo.");
        }

        Usuario usuario = opt.get();

        if (usuario.isEmailVerificado()) {
            return ApiResponse.success(null, "Tu cuenta ya está verificada.");
        }

        String nuevoToken = UUID.randomUUID().toString();
        usuario.setTokenVerificacion(nuevoToken);
        usuario.setTokenExpiracion(LocalDateTime.now().plusHours(24));
        usuarioRepository.save(usuario);

        emailservice.enviarEmailVerificacion(
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                nuevoToken);

        return ApiResponse.success(null, "Se reenviaron las instrucciones a tu correo.");
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
