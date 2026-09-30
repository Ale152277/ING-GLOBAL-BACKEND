package com.ingenieraglobal.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ingenieraglobal.ecommerce.models.Venta;

public class VentaDTO {

    private Long id;
    private Long carritoId;
    private Long usuarioId;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private String estado;
    private List<DetalleVentaDTO> detalles;

    public VentaDTO(Venta venta) {
        this.id = venta.getId();

        if (venta.getCarrito() != null) {
            this.carritoId = venta.getCarrito().getId();
        }

        this.usuarioId = venta.getUsuario().getId();
        this.fechaVenta = venta.getFechaVenta();
        this.total = venta.getTotal();
        this.estado = venta.getEstado().name();

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

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }
}