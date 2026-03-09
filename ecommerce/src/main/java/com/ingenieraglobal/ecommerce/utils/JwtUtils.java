package com.ingenieraglobal.ecommerce.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret:tu_clave_secreta_muy_segura_aqui_con_minimo_32_caracteres_para_jwt}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private int jwtExpiration; // en milisegundos(24 horas)

    // firma, valida y protege la integridad del token
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Genera un token JWT para un usuario
     * 
     * @param usuarioId ID del usuario
     * @param email     Email del usuario
     * @return Token JWT
     */
    public String generarToken(Long usuarioId, String email, String rol) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(email) // identidad principal
                .claim("rol", rol)
                .claim("usuarioId", usuarioId) // Claim personalizado
                .issuedAt(now) // fecha creación
                .expiration(expiryDate) // fecha expiracion
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Valida si un token JWT es válido
     * 
     * @param token Token a validar
     * @return true si es válido, false si no
     */

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae el email del token JWT
     * 
     * @param token Token JWT
     * @return Email del usuario
     */

    public String obtenerEmailDelToken(String token) {
        try {
            return obtenerClaims(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extrae el ID del usuario del token JWT
     * 
     * @param token Token JWT
     * @return ID del usuario
     * 
     *         aqui se puede guardar roles, permisos, etc...
     * 
     */
    public Long obtenerUsuarioIdDelToken(String token) {
        try {
            return obtenerClaims(token).get("usuarioId", Long.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica si un token ha expirado
     * 
     * @param token Token JWT
     * @return true si ha expirado, false si aún es válido
     */

    public boolean tokenExpirado(String token) {
        try {
            return obtenerClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
