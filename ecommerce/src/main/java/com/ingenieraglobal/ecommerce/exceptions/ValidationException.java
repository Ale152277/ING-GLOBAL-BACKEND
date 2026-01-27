package com.ingenieraglobal.ecommerce.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String mensaje){
        super(mensaje);
    }

    public ValidationException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }
    
}
