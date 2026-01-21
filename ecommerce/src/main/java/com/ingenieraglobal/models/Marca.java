package com.ingenieraglobal.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.ingenieraglobal.models.enums.EstadoEnum;

@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String logo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado = EstadoEnum.ACTIVO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "marca")
    private List<Producto> productos;

    public Marca() {
    }

    public Marca(String nombre) {
        this.nombre = nombre;
        this.estado = EstadoEnum.ACTIVO;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Producto> getProductos() {
        return productos;
    }

}
