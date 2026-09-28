package com.ingenieraglobal.ecommerce.controllers;

import com.ingenieraglobal.ecommerce.dtos.request.ConsultaRequest;
import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;
import com.ingenieraglobal.ecommerce.models.Usuario;
import com.ingenieraglobal.ecommerce.services.Emailservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.ingenieraglobal.ecommerce.repositories.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/consultas")
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultaController {

    @Autowired
    private Emailservice emailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${app.mail.destinatario}")
    private String destinatario;

    @PostMapping("/enviar")
    public ResponseEntity<ApiResponse<String>> enviarConsulta(
            @Valid @RequestBody ConsultaRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Long usuarioId = Long.parseLong(auth.getName());

        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        
        emailService.enviarConsulta(
                destinatario,
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                request.getAsunto(),
                request.getMensaje());

        return ResponseEntity.ok(ApiResponse.success("Consulta enviada correctamente"));
    }
}
