package com.ingenieraglobal.dtos.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AgregarAlCarritoRequest {
    @NotNull(message = "El ID del producto es requerido")
    private Long productoId;
    
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}