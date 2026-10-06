package com.ingenieraglobal.ecommerce.dtos.admin;

import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;

public class ActualizarEstadoVentaDTO {
    private EstadoVentaEnum estado;

    public EstadoVentaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoVentaEnum estado) {
        this.estado = estado;
    }
}
