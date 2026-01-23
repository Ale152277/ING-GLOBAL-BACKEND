package com.ingenieraglobal.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ingenieraglobal.ecommerce.models.Carrito;

public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvioWhatsapp;
    private List<DetalleCarritoDTO> detalles;
    private Integer cantidadProductos;
    private BigDecimal total;

    public CarritoDTO(Carrito c) {
        this.id = c.getId();
        this.usuarioId = c.getUsuario().getId();
        this.estado = c.getEstado().name();
        this.fechaCreacion = c.getFechaCreacion();
        this.fechaEnvioWhatsapp = c.getFechaEnvioWhatsapp();
        if (c.getDetalles() != null) {
            this.detalles = c.getDetalles().stream()
                    .map(DetalleCarritoDTO::new)
                    .collect(Collectors.toList());
        }
        this.cantidadProductos = c.getCantidadProductos();
        this.total = c.getTotal();
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaEnvioWhatsapp() {
        return fechaEnvioWhatsapp;
    }

    public List<DetalleCarritoDTO> getDetalles() {
        return detalles;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public BigDecimal getTotal() {
        return total;
    }

}
