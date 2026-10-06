package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.MetodoPagoEnum;

@Entity
@Table(name = "ventas")

public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<DetalleVenta> detalles = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVentaEnum estado = EstadoVentaEnum.PENDIENTE;

    @Column(name = "nombre_receptor",  length = 100)
    private String nombreReceptor;

    @Column(name = "telefono_entrega", length = 20)
    private String telefonoEntrega;

    @Column(name = "direccion_entrega", length = 255)
    private String direccionEntrega;

    @Column(name = "referencia_entrega", length = 255)
    private String referenciaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago" )
    private MetodoPagoEnum metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago")
    private EstadoPagoEnum estadoPago = EstadoPagoEnum.PENDIENTE;

    public Venta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoVentaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoVentaEnum estado) {
        this.estado = estado;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
        detalle.setVenta(this);
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public void setNombreReceptor(String nombreReceptor) {
        this.nombreReceptor = nombreReceptor;
    }

    public String getTelefonoEntrega() {
        return telefonoEntrega;
    }

    public void setTelefonoEntrega(String telefonoEntrega) {
        this.telefonoEntrega = telefonoEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getReferenciaEntrega() {
        return referenciaEntrega;
    }

    public void setReferenciaEntrega(String referenciaEntrega) {
        this.referenciaEntrega = referenciaEntrega;
    }

    public MetodoPagoEnum getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoEnum metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstadoPagoEnum getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPagoEnum estadoPago) {
        this.estadoPago = estadoPago;
    }

}
