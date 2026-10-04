package com.ingenieraglobal.ecommerce.dtos.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ingenieraglobal.ecommerce.models.Venta;

public class PedidoRecienteDTO {

    private Long id;
    private String cliente;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;

    public PedidoRecienteDTO(Venta venta) {

        this.id = venta.getId();

        this.cliente = venta.getUsuario().getNombreCompleto();

        this.fecha = venta.getFechaVenta();

        this.total = venta.getTotal();

        this.estado = venta.getEstado().name();
    }

    public Long getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }
}