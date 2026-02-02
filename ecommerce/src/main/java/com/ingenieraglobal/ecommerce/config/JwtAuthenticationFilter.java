package com.ingenieraglobal.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("=== NUEVA PETICIÓN ===");
        System.out.println("URL: " + request.getRequestURI());
        System.out.println("Authorization Header: "
                + (authHeader != null ? authHeader.substring(0, Math.min(50, authHeader.length())) + "..." : "NULL"));
        System.out.println("=== FIN ===");

        try {
            // Extrae el token del header authorization y lo almacena en jwt
            String jwt = getJwtFromRequest(request);
            System.out.println("🔍 JWT extraído: " + (jwt != null ? "✅ Sí" : "❌ No"));

            // si jwt es diferente a null y es valido el token
            if (jwt != null && jwtUtils.validarToken(jwt)) {
                System.out.println("✅ Token válido");

                // se extrae el email del token
                String email = jwtUtils.obtenerEmailDelToken(jwt);
                System.out.println("📧 Email del token: " + email);

                // busca el usuario en la BD mediante el email
                var usuario = usuarioRepository.findByEmail(email);
                System.out.println("👤 Usuario encontrado: " + usuario.get().getEmail());

                // si el usuario está presente
                if (usuario.isPresent()) {
                    // se obtiene al usuario
                    Usuario user = usuario.get();

                    // se crea la autenticacion con rol
                    List<GrantedAuthority> authorities = List
                            .of(new SimpleGrantedAuthority("ROLE_" + user.getRol().name()));

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user.getId(),
                            null,
                            authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("❌ Usuario NO encontrado");

                }

            } else {
                System.out.println("❌ Token inválido o no existe");

            }

        } catch (Exception ex) {
            System.out.println("❌ Error en filtro: " + ex.getMessage());

            logger.error("no se puedo establecer la autenticacion del usuario", ex);
        }
        filterChain.doFilter(request, response);
    }

    // * Extrae el token JWT del header Authorization
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // remover bearer
        }
        return null;
    }

}
