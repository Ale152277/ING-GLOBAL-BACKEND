package com.ingenieraglobal.ecommerce.dtos.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class ConsultaRequest {
    @NotBlank(message = "El asunto es obligatorio")
    @Size(min = 10, max =150)
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min= 10, max =2000)
    private String mensaje;
    

    public String getAsunto(){
        return asunto;
    }

    public void setAsunto(String asunto){
        this.asunto = asunto;
    }

    public String getMensaje(){
        return mensaje;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
        
    }
}
