package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;

@Entity
@Table(name = "distritos")
public class Distrito {

    @Id
    @Column(length = 6)
    private String ubigeo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_provincia", nullable = false)
    private Provincia provincia;

    public Distrito() {}

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
}