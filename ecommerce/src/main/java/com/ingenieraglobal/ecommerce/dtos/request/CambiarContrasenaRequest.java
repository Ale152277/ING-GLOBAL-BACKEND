package com.ingenieraglobal.ecommerce.dtos.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CambiarContrasenaRequest {
    @NotBlank (message = "la contraseña actual es obligatoria")
    private String contrasenaActual;

    @NotBlank (message = "la nueva contraseña es obligatoria")
    @Size (
        min= 8,
        max= 72,
        message = "la contraseña debe contener entre 8 y 72 caracteres + caracteres especiales"
    )
    private String contrasenaNueva;

    public String getContrasenaActual (){
        return contrasenaActual;
    }

    public void setContrasenaActual (String contrasenaActual){
        this.contrasenaActual = contrasenaActual;
    }

    public String getContrasenaNueva(){
        return contrasenaNueva;
    }

    public void setContrasenaNueva (String contrasenaNueva){
        this.contrasenaNueva = contrasenaNueva;
    }





}
