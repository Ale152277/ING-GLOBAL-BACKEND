package com.ingenieraglobal.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration // esta clase define configuracion del sistema
@EnableWebSecurity // activa SpringSecurity, de lo contrario no hay seguridad
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() { // define como se encriptan las contraseñas
        return new BCryptPasswordEncoder(); // Usa bcryps para encriptar
    }

    @Bean // spring lo crea una vez (cada que vez que guarde o cree una contraseña usaré
          // este encoder)

    // aqui se define que rutas están protegidas o son publicas o cuales requieren
    // Login
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // deshabilitar crsf
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))// permite que angular pueda llamar a
                                                                                  // nuestro backend
                .authorizeHttpRequests(auth -> auth // aqui se decide que puede acceder a que ruta
                        // Endpoints públicos
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()  

                        .requestMatchers(HttpMethod.POST, "/api/v1/usuario/registro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuario/registro/admin").permitAll()
                        .requestMatchers("/api/v1/productos/**").permitAll()
                        .requestMatchers("/api/v1/categorias/**").permitAll()
                        .requestMatchers("/api/v1/marcas/**").permitAll()

                        // Endpoints protegidos
                        .requestMatchers("/api/v1/carrito/**").authenticated()
                        .requestMatchers("/api/v1/usuario/**").authenticated()

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated())
                // usar stateless session (JWT)
                // no guarda sesiones, cada request debe traer su token JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // solo angular puede acceder (4200)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permite enviar tokens, cookies, auth headers
        configuration.setAllowCredentials(true);

        // el navegador cuarda el config por 1 hora
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
