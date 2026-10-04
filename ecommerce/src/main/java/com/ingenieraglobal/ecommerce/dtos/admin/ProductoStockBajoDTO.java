package com.ingenieraglobal.ecommerce.dtos.admin;

import com.ingenieraglobal.ecommerce.models.Producto;

public class ProductoStockBajoDTO {

    private Long id;
    private String nombre;
    private String sku;
    private Integer stock;
    private String imagen;

    public ProductoStockBajoDTO(
            Producto producto) {

        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.sku = producto.getSku();
        this.stock = producto.getStock();
        this.imagen = producto.getImagen();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSku() {
        return sku;
    }

    public Integer getStock() {
        return stock;
    }

    public String getImagen() {
        return imagen;
    }
}