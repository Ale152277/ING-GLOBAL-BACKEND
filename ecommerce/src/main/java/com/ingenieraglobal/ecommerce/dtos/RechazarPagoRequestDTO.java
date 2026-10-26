package com.ingenieraglobal.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RechazarPagoRequestDTO {

    @NotBlank(message = "El motivo de rechazo es obligatorio")
    @Size(max = 500, message = "El motivo de rechazo no puede superar los 500 caracteres")
    private String motivo;

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
