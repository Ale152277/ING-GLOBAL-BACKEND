package com.ingenieraglobal.ecommerce.dtos.admin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ingenieraglobal.ecommerce.models.Venta;

public class AdminVentaResumenDTO {
    private Long id;
    private String cliente;
    private String email;
    private String telefono;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private String estado;

    public AdminVentaResumenDTO(Venta venta) {
        this.id = venta.getId();
        this.cliente = venta.getUsuario().getNombreCompleto();
        this.email = venta.getUsuario().getEmail();
        this.telefono = venta.getUsuario().getTelefono();
        this.fechaVenta = venta.getFechaVenta();
        this.total = venta.getTotal();
        this.estado = venta.getEstado().name();
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
}
