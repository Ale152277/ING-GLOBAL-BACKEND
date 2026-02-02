package com.ingenieraglobal.ecommerce.dtos.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public class CrearProductoRequest {

    @NotBlank (message = "El nombre del producto es obligatorio")
    @Size(min=3, max= 150, message = "El nombre del producto debe tener entre 3 y 150 caracteres")
    private String nombre;

    @NotBlank(message = "El SKU es obigatorio")
    @Size(min = 3, max = 50, message = "El SKU debe tener entre 3 y 50 caracteres")
    private String sku;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "99999.99", message = "El precio no puede ser mayor 99999.99")
    private BigDecimal precio;

    @NotNull(message = "La categoria es obligatoria")
    private Long categoriaId;

    private Long marcaId;

    @Size(max = 255, message = "La descripcion no puede exceder los 255 caracteres")
    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 10000, message = "El stock no puede ser mayor a 10,000")
    private Integer stock = 0;


    @Min(value = 0, message = "El descuento no puede ser negativo")
    @Max(value = 100, message = "El descuento no puede ser mayor a 100")
    private Integer descuento = 0;

    @Size(max = 50, message = "La etiqueta no puede exceder los 50 caracteres")
    private String etiqueta;

    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres")
    private String imagen;

    @DecimalMin(value = "0.0", message = "El rating no puede ser menor a 0.0")
    @DecimalMax(value = "5.0", message = "El rating no puede ser mayor a 5.0")
    private BigDecimal rating;
    
     public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
}
