package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;


@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    // Se reemplazará por @ManyToOne a Venta cuando se integre
    @Column(name = "id_venta")
    private Long ventaId;

    @Column(nullable = false, length = 50)
    private String tipoPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estadoPago = EstadoEnum.ACTIVO;

    public Pago() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVentaId() { return ventaId; }
    public void setVentaId(Long ventaId) { this.ventaId = ventaId; }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public EstadoEnum getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoEnum estadoPago) { this.estadoPago = estadoPago; }
}