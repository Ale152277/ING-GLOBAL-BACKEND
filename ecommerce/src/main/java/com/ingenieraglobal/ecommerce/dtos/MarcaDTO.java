package com.ingenieraglobal.ecommerce.dtos;

import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.models.Marca;

public class MarcaDTO {
    private Long id;

    private String nombre;
    private String logo;

    private EstadoEnum estado;

    public MarcaDTO(Marca marca) {
        this.id = marca.getId();
        this.nombre = marca.getNombre();
        this.logo = marca.getLogo();
        this.estado = marca.getEstado();
    }

    // Constructor vacío
    public MarcaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public EstadoEnum getEstado() {
        return estado;

    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

}
