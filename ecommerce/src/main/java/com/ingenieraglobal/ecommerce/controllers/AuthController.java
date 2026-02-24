package com.ingenieraglobal.ecommerce.controllers;
//import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ingenieraglobal.ecommerce.dtos.request.LoginRequest;
import com.ingenieraglobal.ecommerce.dtos.request.RegistroUsuarioRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.dtos.response.TokenResponse;
import com.ingenieraglobal.ecommerce.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200") 

public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<String>>registrar(@Valid @RequestBody RegistroUsuarioRequest request){
        ApiResponse<String> response = authService.registrar(request);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request ){
        ApiResponse<TokenResponse> response = authService.login(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    // El usuario hace clic en el link del email → Angular llama este endpoint
    @GetMapping("/verificar")
    public ResponseEntity <ApiResponse<String>> verificarEmail(@RequestParam String token){
        ApiResponse<String> response = authService.verificarEmail(token);

        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    } 


    @PostMapping("/reenviar-verificacion")
    public ResponseEntity<ApiResponse<String>> reenviarVerificacion (@RequestParam String email){
        ApiResponse<String> response = authService.reenviarVerificacion(email);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }


    }



    
    
}
