package com.ingenieraglobal.ecommerce.dtos;

import com.ingenieraglobal.ecommerce.models.enums.MetodoPagoEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CrearVentaRequestDTO {

    @NotBlank(message = "El nombre del receptor es obligatorio")
    @Size(max = 100)
    private String nombreReceptor;

    @NotBlank(message = "El teléfono de entrega es obligatorio")
    @Size(max = 20)
    private String telefonoEntrega;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 255)
    private String direccionEntrega;

    @Size(max = 255)
    private String referenciaEntrega;

    @NotNull(message = "Debes seleccionar un método de pago")
    private MetodoPagoEnum metodoPago;

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
}