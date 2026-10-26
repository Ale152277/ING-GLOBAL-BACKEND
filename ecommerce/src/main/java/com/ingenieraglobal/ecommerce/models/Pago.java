package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.MetodoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.ProveedorPagoEnum;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPagoEnum metodoPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPagoEnum estadoPago = EstadoPagoEnum.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "proveedor_pago", nullable = false)
    private ProveedorPagoEnum proveedorPago;

    @Column(name = "referencia_externa", length = 100)
    private String referenciaExterna;

    @Column(name = "comprobante_referencia", length = 500)
    private String comprobanteReferencia;

    @Column(name = "fecha_comprobante")
    private LocalDateTime fechaComprobante;

    @Column(name = "motivo_rechazo", length = 500)
    private String motivoRechazo;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    public Pago() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public MetodoPagoEnum getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoEnum metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public EstadoPagoEnum getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPagoEnum estadoPago) {
        this.estadoPago = estadoPago;
    }

    public ProveedorPagoEnum getProveedorPago() {
        return proveedorPago;
    }

    public void setProveedorPago(ProveedorPagoEnum proveedorPago) {
        this.proveedorPago = proveedorPago;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public String getComprobanteReferencia() {
        return comprobanteReferencia;
    }

    public void setComprobanteReferencia(String comprobanteReferencia) {
        this.comprobanteReferencia = comprobanteReferencia;
    }

    public LocalDateTime getFechaComprobante() {
        return fechaComprobante;
    }

    public void setFechaComprobante(LocalDateTime fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public LocalDateTime getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(LocalDateTime fechaRevision) {
        this.fechaRevision = fechaRevision;
    }
}
