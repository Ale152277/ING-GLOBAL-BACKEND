package com.ingenieraglobal.models.enums;

public enum EstadoCarritoEnum {
    ACTIVO("activo"),
    INACTIVO("inactivo"),
    CANCELADO("cancelado");

    private String valor;
    EstadoCarritoEnum(String valor){this.valor = valor;}
    public String getValor(){return valor;}


    
}
