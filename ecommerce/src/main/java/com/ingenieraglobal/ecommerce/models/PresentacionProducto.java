package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;

@Entity
@Table(name = "presentacion_producto")
public class PresentacionProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_presentacionProducto")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipoPresentacion", nullable = true)
    private TipoPresentacion tipoPresentacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipoUnidad", nullable = true)
    private TipoUnidad tipoUnidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadBase;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado = EstadoEnum.ACTIVO;

    @Column(length = 255)
    private String imagen;

    public PresentacionProducto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public TipoPresentacion getTipoPresentacion() {
        return tipoPresentacion;
    }

    public void setTipoPresentacion(TipoPresentacion t) {
        this.tipoPresentacion = t;
    }

    public TipoUnidad getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(TipoUnidad t) {
        this.tipoUnidad = t;
    }

    public BigDecimal getCantidadBase() {
        return cantidadBase;
    }

    public void setCantidadBase(BigDecimal cantidadBase) {
        this.cantidadBase = cantidadBase;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}
