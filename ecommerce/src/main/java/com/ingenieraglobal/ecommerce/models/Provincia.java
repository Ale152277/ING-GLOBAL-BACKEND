package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;

@Entity
@Table(name = "provincias")
public class Provincia {

    @Id
    @Column(length = 4)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_departamento", nullable = false)
    private Departamento departamento;

    public Provincia() {}

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

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}