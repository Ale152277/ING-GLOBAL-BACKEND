package com.ingenieraglobal.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ingenieraglobal.ecommerce.models.Pago;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.MetodoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.ProveedorPagoEnum;

public class AdminPagoDTO {

    private Long id;
    private Long ventaId;

    private Long usuarioId;
    private String nombreCliente;

    private MetodoPagoEnum metodoPago;
    private ProveedorPagoEnum proveedorPago;
    private EstadoPagoEnum estadoPago;

    private BigDecimal monto;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaComprobante;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaExpiracionPago;

    private boolean comprobanteDisponible;

    public AdminPagoDTO(Pago pago) {
        this.id = pago.getId();
        this.ventaId = pago.getVenta().getId();
        this.usuarioId = pago.getVenta().getUsuario().getId();
        this.metodoPago = pago.getMetodoPago();
        this.proveedorPago = pago.getProveedorPago();
        this.estadoPago = pago.getEstadoPago();
        this.monto = pago.getMonto();
        this.fechaCreacion = pago.getFechaCreacion();
        this.fechaComprobante = pago.getFechaComprobante();
        this.fechaPago = pago.getFechaPago();
        this.fechaExpiracionPago = pago.getVenta().getFechaExpiracionPago();
        this.comprobanteDisponible = pago.getComprobanteReferencia() != null;

    }

    public Long getId() {
        return id;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public MetodoPagoEnum getMetodoPago() {
        return metodoPago;
    }

    public ProveedorPagoEnum getProveedorPago() {
        return proveedorPago;
    }

    public EstadoPagoEnum getEstadoPago() {
        return estadoPago;
    }

    public BigDecimal getMonto() {
        return monto;
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

    public boolean isComprobanteDisponible() {
        return comprobanteDisponible;
    }
}
