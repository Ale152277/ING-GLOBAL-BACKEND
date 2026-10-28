package com.ingenieraglobal.ecommerce.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
public class ActualizarPerfilRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombreCompleto;

    @Pattern(
        regexp = "^$|^[0-9]{9}$",
        message = "El teléfono debe tener 9 dígitos"
    )
    private String telefono;

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
