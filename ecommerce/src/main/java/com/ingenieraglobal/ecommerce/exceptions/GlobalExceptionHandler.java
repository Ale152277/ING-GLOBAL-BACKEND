package com.ingenieraglobal.ecommerce.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError; //representa eeror de campo especifico
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ingenieraglobal.ecommerce.dtos.response.ApiResponse;

import java.util.HashMap;
import java.util.Map;




@RestControllerAdvice
//esta clase escucha errores y response en JSON. Aplica todo el backend
public class GlobalExceptionHandler {

    //maneja excepcion de validacion valid si no cumple (si llegara vacio email por ejemplo)
    //cuando ocurra una excepción, spring llama automaticamente al metodo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex, WebRequest request){

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.error("Error de validación", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } 

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiResponse<String>> hanldeRecursoNoEncontradoException(RecursoNoEncontradoException ex, WebRequest request){
        ApiResponse<String> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    //maneja excepciones de validacion de negocio
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(ValidationException ex, WebRequest request){
        ApiResponse<String> response = ApiResponse.error((ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //maneja cualquier otra excepcion no capturada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex, WebRequest request){
        ApiResponse<String> response = ApiResponse.error("ERROR INTERNO DEL SERVIDOR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    

    

    
}
