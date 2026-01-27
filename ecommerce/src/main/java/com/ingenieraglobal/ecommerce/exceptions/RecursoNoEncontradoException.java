package com.ingenieraglobal.ecommerce.exceptions;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje){
        super(mensaje);
    }

    public RecursoNoEncontradoException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }
    
    
    
}
