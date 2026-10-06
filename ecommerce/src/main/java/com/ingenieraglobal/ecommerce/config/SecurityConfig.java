package com.ingenieraglobal.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

@Configuration // esta clase define configuracion del sistema
@EnableWebSecurity // activa SpringSecurity, de lo contrario no hay seguridad
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() { // define como se encriptan las contraseñas
        return new BCryptPasswordEncoder();
    }

    @Bean

    // aqui se define que rutas están protegidas o son publicas o cuales requieren
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // deshabilitar crsf
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            response.getWriter().write(
                                    "{\"success\":false,\"message\":\"No autenticado\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            response.getWriter().write(
                                    "{\"success\":false,\"message\":\"Acceso denegado\"}");
                        }))
                .authorizeHttpRequests(auth -> auth
                        // edpoints publicos
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // cors

                        .requestMatchers("/api/health").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/registro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/verificar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/reenviar-verificacion").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/marcas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/marcas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/marcas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/marcas/**").hasRole("ADMIN")

                        // endpoints protegidos
                        .requestMatchers("/api/v1/carrito/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuario/registro/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/usuario/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/usuario/**").authenticated()

                        .requestMatchers("/api/v1/consultas/**").authenticated()

                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200",
                "https://ingenieriaglobalperu.com",
                "https://www.ingenieriaglobalperu.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
