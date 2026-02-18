package com.ingenieraglobal.ecommerce.models;

import jakarta.persistence.*;
import java.util.List;


import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.models.enums.RolEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "contraseña_hash", nullable = false, length = 255)
    private String contraseñaHash;

    @Column(length = 20)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolEnum rol = RolEnum.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado = EstadoEnum.ACTIVO;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Carrito> carritos;

    @Column(name= "email_verificado", nullable = false)
    private boolean emailVerificado = false;

    @Column(name = "token_verificacion", length = 255)
    private String tokenVerificacion;

    @Column(name = "toxen_expiracion")
    private LocalDateTime tokenExpiracion;

    public Usuario() {
    }

    public Usuario(String nombreCompleto, String email, String contraseñaHash) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.contraseñaHash = contraseñaHash;
        this.rol = RolEnum.USER;
        this.estado = EstadoEnum.ACTIVO;
        this.emailVerificado = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseñaHash() {
        return contraseñaHash;
    }

    public void setContraseñaHash(String contraseñaHash) {
        this.contraseñaHash = contraseñaHash;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public RolEnum getRol() {
        return rol;
    }

    public void setRol(RolEnum rol) {
        this.rol = rol;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion){
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<Carrito> getCarritos() {
        return carritos;
    }

    public boolean isEmailVerificado(){
        return emailVerificado;
    }

    public void setEmailVerificado (boolean emailVerificado){
        this.emailVerificado = emailVerificado;
    }

    public String getTokenVerficacion (){
        return tokenVerificacion;
    }

    public void setTokenVerificacion (String tokenVerificacion){
        this.tokenVerificacion = tokenVerificacion;
    }

    public LocalDateTime getTokenExpiracion(){
        return tokenExpiracion;
    }

    public void setTokenExpiracion(LocalDateTime tokenExpiracion){
        this.tokenExpiracion = tokenExpiracion;

    }


}
