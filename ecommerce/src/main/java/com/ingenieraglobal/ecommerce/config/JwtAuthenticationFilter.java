package com.ingenieraglobal.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ingenieraglobal.ecommerce.models.Usuario;
import com.ingenieraglobal.ecommerce.repositories.UsuarioRepository;
import com.ingenieraglobal.ecommerce.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Extrae el token del header authorization y lo almacena en jwt
            String jwt = getJwtFromRequest(request);

            // si jwt es diferente a null y es valido el token
            if (jwt != null && jwtUtils.validarToken(jwt)) {

                // se extrae el email del token
                String email = jwtUtils.obtenerEmailDelToken(jwt);

                // busca el usuario en la BD mediante el email
                var usuario = usuarioRepository.findByEmail(email);

                // si el usuario está presente
                if (usuario.isPresent()) {
                    // se obtiene al usuario
                    Usuario user = usuario.get();

                    // se crea la autenticacion
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user.getId(), null, java.util.Collections.emptyList());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }

        } catch (Exception ex) {
            logger.error("no se puedo establecer la autenticacion del usuario", ex);
        }
        filterChain.doFilter(request, response);
    }

    // * Extrae el token JWT del header Authorization
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); //remover bearer
        }
        return null;
    }

}
