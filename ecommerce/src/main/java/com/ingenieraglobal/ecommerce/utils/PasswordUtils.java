package com.ingenieraglobal.ecommerce.utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//clase encargada de encriptar y verificar contraseñas
//evita repitir codigo en servicios y controladores

@Component
public class PasswordUtils {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //encripta la contraseña en texto plano
    public String encriptarContraseña (String passwordPlain){
        return passwordEncoder.encode(passwordPlain);
    }


    //verifica si una contraseña en texto plano coincide con su hash
    public boolean verificarContraseña (String passwordPlain, String passwordHash){
        return passwordEncoder.matches(passwordPlain, passwordHash);
    }
    
}
