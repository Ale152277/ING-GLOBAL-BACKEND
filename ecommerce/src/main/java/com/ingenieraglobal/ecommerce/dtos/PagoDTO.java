package com.ingenieraglobal.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ingenieraglobal.ecommerce.models.Pago;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.MetodoPagoEnum;

public class PagoDTO {

    private Long id;
    private Long ventaId;
    private MetodoPagoEnum metodoPago;
    private BigDecimal monto;
    private EstadoPagoEnum estadoPago;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaComprobante;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaExpiracionPago;
    private LocalDateTime fechaRevision;

    private boolean comprobanteEnviado;
    private String motivoRechazo;

    public PagoDTO(Pago pago) {
        this.id = pago.getId();
        this.ventaId = pago.getVenta().getId();
        this.metodoPago = pago.getMetodoPago();
        this.monto = pago.getMonto();
        this.estadoPago = pago.getEstadoPago();
        this.fechaCreacion = pago.getFechaCreacion();
        this.fechaComprobante = pago.getFechaComprobante();
        this.fechaPago = pago.getFechaPago();
        this.fechaExpiracionPago = pago.getVenta().getFechaExpiracionPago();
        this.fechaRevision = pago.getFechaRevision();
        this.comprobanteEnviado = pago.getComprobanteReferencia() != null;
        this.motivoRechazo = pago.getMotivoRechazo();
    }

    public Long getId() {
        return id;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public MetodoPagoEnum getMetodoPago() {
        return metodoPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public EstadoPagoEnum getEstadoPago() {
        return estadoPago;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaComprobante() {
        return fechaComprobante;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public LocalDateTime getFechaExpiracionPago() {
        return fechaExpiracionPago;
    }

    public LocalDateTime getFechaRevision() {
        return fechaRevision;
    }

    public boolean isComprobanteEnviado() {
        return comprobanteEnviado;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }
}
