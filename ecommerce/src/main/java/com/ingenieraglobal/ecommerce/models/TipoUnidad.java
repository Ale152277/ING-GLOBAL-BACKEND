package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;


@Entity
@Table(name= "tipo_unidad")
public class TipoUnidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipoUnidad")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String simbolo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado = EstadoEnum.ACTIVO;
    
    public TipoUnidad(){}

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id= id ;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getSimbolo(){
        return simbolo;
    }

    public void setSimbolo(String simbolo){
        this.simbolo = simbolo;
    }

    public EstadoEnum getEstado(){
        return estado;
    }

    public void setEstado(EstadoEnum estado){
        this.estado = estado;
    }
    
}
