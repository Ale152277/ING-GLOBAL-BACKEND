package com.ingenieraglobal.ecommerce.dtos;
import java.math.BigDecimal;

import com.ingenieraglobal.ecommerce.models.DetalleCarrito;

public class DetalleCarritoDTO {
    private Long id;
    private Long carritoId;
    private ProductoDTO producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private Integer descuentoAplicado;
    private BigDecimal subtotal;
    
    public DetalleCarritoDTO(DetalleCarrito dc) {
        this.id = dc.getId();
        this.carritoId = dc.getCarrito().getId();
        this.producto = new ProductoDTO(dc.getProducto());
        this.cantidad = dc.getCantidad();
        this.precioUnitario = dc.getPrecioUnitario();
        this.descuentoAplicado = dc.getDescuentoAplicado();
        this.subtotal = dc.getSubtotal();
    }
    
    public Long getId() { return id; }
    public Long getCarritoId() { return carritoId; }
    public ProductoDTO getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public Integer getDescuentoAplicado() { return descuentoAplicado; }
    public BigDecimal getSubtotal() { return subtotal; }

}
