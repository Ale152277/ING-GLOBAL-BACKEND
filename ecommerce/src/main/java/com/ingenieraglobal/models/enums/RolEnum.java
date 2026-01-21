package com.ingenieraglobal.models.enums;

public enum RolEnum {
    USER("USER"),
    ADMIN("ADMIN");
    
    private String valor;
    RolEnum(String valor) { this.valor = valor; }
    public String getValor() { return valor; }

    
}
