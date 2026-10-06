package com.ingenieraglobal.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ingenieraglobal.ecommerce.dtos.DetalleVentaDTO;
import com.ingenieraglobal.ecommerce.models.Venta;

public class AdminVentaDetalleDTO {

    private Long id;

    private String cliente;
    private String email;
    private String telefono;

    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private String estado;

    private String nombreReceptor;
    private String telefonoEntrega;
    private String direccionEntrega;
    private String referenciaEntrega;

    private String metodoPago;
    private String estadoPago;

    private List<DetalleVentaDTO> detalles;

    public AdminVentaDetalleDTO(Venta venta) {

        this.id = venta.getId();

        this.cliente = venta.getUsuario().getNombreCompleto();

        this.email = venta.getUsuario().getEmail();

        this.telefono = venta.getUsuario().getTelefono();

        this.fechaVenta = venta.getFechaVenta();

        this.total = venta.getTotal();

        this.estado = venta.getEstado().name();

        this.nombreReceptor = venta.getNombreReceptor();

        this.telefonoEntrega = venta.getTelefonoEntrega();

        this.direccionEntrega = venta.getDireccionEntrega();

        this.referenciaEntrega = venta.getReferenciaEntrega();

        this.metodoPago = venta.getMetodoPago() != null
                ? venta.getMetodoPago().name()
                : null;

        this.estadoPago = venta.getEstadoPago() != null
                ? venta.getEstadoPago().name()
                : null;

        this.detalles = venta.getDetalles()
                .stream()
                .map(DetalleVentaDTO::new)
                .toList();
    }

    public Long getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
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

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }
}