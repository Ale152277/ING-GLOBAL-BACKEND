package com.ingenieraglobal.ecommerce.dtos;

import java.math.BigDecimal;

import com.ingenieraglobal.ecommerce.models.DetalleVenta;

public class DetalleVentaDTO {

    private Long id;
    private Long productoId;
    private String nombreProducto;
    private String sku;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private Integer descuentoAplicado;
    private BigDecimal subtotal;
    private String imagenProducto;

    public DetalleVentaDTO(DetalleVenta detalle) {
        this.id = detalle.getId();

        if (detalle.getProducto() != null) {
            this.productoId = detalle.getProducto().getId();
        }

        this.nombreProducto = detalle.getNombreProducto();
        this.sku = detalle.getSku();
        this.cantidad = detalle.getCantidad();
        this.precioUnitario = detalle.getPrecioUnitario();
        this.descuentoAplicado = detalle.getDescuentoAplicado();
        this.subtotal = detalle.getSubtotal();
        this.imagenProducto = detalle.getImagenProducto();
    }

    public Long getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getSku() {
        return sku;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public Integer getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }
}