package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;

@Entity
@Table(name = "departamentos")
public class Departamento {

    @Id
    @Column(length = 2)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    public Departamento() {}

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



}