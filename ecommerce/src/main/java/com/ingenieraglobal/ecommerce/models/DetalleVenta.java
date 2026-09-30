package com.ingenieraglobal.ecommerce.models;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity 
@Table(name = "detalle_venta")

public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "descuento_aplicado", nullable = false)
    private Integer descuentoAplicado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public DetalleVenta() {
    }

    public DetalleVenta(Venta venta, DetalleCarrito detalleCarrito) {
        this.venta = venta;
        this.producto = detalleCarrito.getProducto();
        this.nombreProducto = detalleCarrito.getProducto().getNombre();
        this.sku = detalleCarrito.getProducto().getSku();
        this.cantidad = detalleCarrito.getCantidad();
        this.precioUnitario = detalleCarrito.getPrecioUnitario();
        this.descuentoAplicado = detalleCarrito.getDescuentoAplicado();
        this.subtotal = detalleCarrito.getSubtotal();
    }

    public Long getId() {
        return id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(Integer descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}