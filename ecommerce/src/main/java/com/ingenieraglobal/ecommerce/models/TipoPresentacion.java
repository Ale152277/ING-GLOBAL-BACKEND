package com.ingenieraglobal.ecommerce.models;
import jakarta.persistence.*;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;

@Entity
@Table(name = "tipos_presentacion")
public class TipoPresentacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipoPresentacion")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado = EstadoEnum.ACTIVO;
    

    public TipoPresentacion(){}
    
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getDescription(){
        return descripcion;
    }

    public void setDescription (String description){
        this.descripcion = description;
    }

    public EstadoEnum getEstado(){
        return estado;
    }

    public void setEstado(EstadoEnum estado){
        this.estado =  estado;
    }
    
}
