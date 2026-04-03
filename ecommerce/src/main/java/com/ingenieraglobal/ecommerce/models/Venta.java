package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;

@Entity
@Table(name = "ventas")

public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_venta")
    private Long id;

    @Column(name = "id_carrito")
    private Long carritoId;

    @Column(name = "id_usuario")
    private Long usuarioId;

    @Column(nullable = false)
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado = EstadoEnum.ACTIVO;

    public Venta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCarritoId() { return carritoId; }
    public void setCarritoId(Long carritoId) { this.carritoId = carritoId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public EstadoEnum getEstado() { return estado; }
    public void setEstado(EstadoEnum estado) { this.estado = estado; }


    
}
