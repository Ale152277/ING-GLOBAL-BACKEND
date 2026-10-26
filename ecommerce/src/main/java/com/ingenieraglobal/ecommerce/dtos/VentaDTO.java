package com.ingenieraglobal.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ingenieraglobal.ecommerce.models.Venta;

public class VentaDTO {

    private Long id;
    private Long carritoId;
    private Long usuarioId;

    private String nombreCliente;
    private String emailCliente;
    private String telefonoCliente;

    private String nombreReceptor;
    private String telefonoEntrega;
    private String direccionEntrega;
    private String referenciaEntrega;

    private LocalDateTime fechaVenta;
    private LocalDateTime fechaExpiracionPago;

    private BigDecimal total;

    private String estado;
    private String estadoPago;
    private String metodoPago;

    private List<DetalleVentaDTO> detalles;

    public VentaDTO(Venta venta) {
        this.id = venta.getId();

        if (venta.getCarrito() != null) {
            this.carritoId = venta.getCarrito().getId();
        }

        this.usuarioId = venta.getUsuario().getId();
        this.nombreCliente = venta.getUsuario().getNombreCompleto();
        this.emailCliente = venta.getUsuario().getEmail();
        this.telefonoCliente = venta.getUsuario().getTelefono();

        this.nombreReceptor = venta.getNombreReceptor();
        this.telefonoEntrega = venta.getTelefonoEntrega();
        this.direccionEntrega = venta.getDireccionEntrega();
        this.referenciaEntrega = venta.getReferenciaEntrega();

        this.fechaVenta = venta.getFechaVenta();
        this.fechaExpiracionPago = venta.getFechaExpiracionPago();
        this.total = venta.getTotal();
        this.estado = venta.getEstado().name();

        this.estadoPago = venta.getEstadoPago() != null
                ? venta.getEstadoPago().name()
                : null;

        this.metodoPago = venta.getMetodoPago() != null
                ? venta.getMetodoPago().name()
                : null;

        if (venta.getDetalles() != null) {
            this.detalles = venta.getDetalles()
                    .stream()
                    .map(DetalleVentaDTO::new)
                    .toList();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getCarritoId() {
        return carritoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public String getTelefonoEntrega() {
        return telefonoEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public String getReferenciaEntrega() {
        return referenciaEntrega;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public LocalDateTime getFechaExpiracionPago() {
        return fechaExpiracionPago;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }
}
