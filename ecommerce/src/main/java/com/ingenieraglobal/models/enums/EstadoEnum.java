package com.ingenieraglobal.models.enums;

public enum EstadoEnum {
    ACTIVO("activo"),
    INACTIVO("inactivo");

    private String valor;

    EstadoEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

}
