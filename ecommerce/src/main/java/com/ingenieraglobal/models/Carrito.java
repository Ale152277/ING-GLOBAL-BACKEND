package com.ingenieraglobal.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.ingenieraglobal.models.enums.EstadoCarritoEnum;
import java.math.BigDecimal;

@Entity
@Table(name = "carritos")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarritoEnum estado = EstadoCarritoEnum.ACTIVO;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_envio_whatsapp")
    private LocalDateTime fechaEnvioWhatsapp;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCarrito> detalles;

    public Carrito() {
    }

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
        this.estado = EstadoCarritoEnum.ACTIVO;
    }

    public BigDecimal getTotal() {
        if (detalles == null || detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return detalles.stream()
                .map(DetalleCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getCantidadProductos() {
        if (detalles == null || detalles.isEmpty()) {
            return 0;
        }
        return detalles.stream()
                .mapToInt(DetalleCarrito::getCantidad)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoCarritoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoCarritoEnum estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaEnvioWhatsapp() {
        return fechaEnvioWhatsapp;
    }

    public void setFechaEnvioWhatsapp(LocalDateTime fechaEnvioWhatsapp) {
        this.fechaEnvioWhatsapp = fechaEnvioWhatsapp;
    }

    public List<DetalleCarrito> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCarrito> detalles) {
        this.detalles = detalles;
    }
}
