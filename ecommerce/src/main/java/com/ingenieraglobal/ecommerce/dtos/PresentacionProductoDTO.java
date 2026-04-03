package com.ingenieraglobal.ecommerce.dtos;

import java.math.BigDecimal;
import com.ingenieraglobal.ecommerce.models.PresentacionProducto;

public class PresentacionProductoDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private String tipoPresentacion;
    private String tipoUnidad;
    private Long tipoPresentacionId;
    private Long tipoUnidadId;
    private BigDecimal cantidadBase;
    private BigDecimal precio;
    private String estado;
    private String imagen;

    public PresentacionProductoDTO(PresentacionProducto presentacionProducto) {
        this.id = presentacionProducto.getId();
        this.productoId = presentacionProducto.getProducto().getId();
        this.nombreProducto = presentacionProducto.getProducto().getNombre();

        if (presentacionProducto.getTipoPresentacion() != null) {
            this.tipoPresentacionId = presentacionProducto.getTipoPresentacion().getId();
            this.tipoPresentacion = presentacionProducto.getTipoPresentacion().getNombre();
        }

        if (presentacionProducto.getTipoUnidad() != null) {
            this.tipoUnidadId = presentacionProducto.getTipoUnidad().getId();
            this.tipoUnidad = presentacionProducto.getTipoUnidad().getNombre();

        }

        this.cantidadBase = presentacionProducto.getCantidadBase();
        this.precio = presentacionProducto.getPrecio();
        this.estado = presentacionProducto.getEstado().name();
        this.imagen = presentacionProducto.getImagen();

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

    public Long getTipoPresentacionId() {
        return tipoPresentacionId;
    }

    public Long getTipoUnidadId() {
        return tipoUnidadId;
    }

    public String getTipoPresentacion() {
        return tipoPresentacion;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public BigDecimal getCantidadBase() {
        return cantidadBase;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }

    public String getImagen() {
        return imagen;
    }

}
