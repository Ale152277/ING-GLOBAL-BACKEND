package com.ingenieraglobal.dtos;
import com.ingenieraglobal.models.Categoria;

public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String slug;
    private Integer orden;
    private Integer productosCount;
    
    public CategoriaDTO(Categoria c) {
        this.id = c.getId();
        this.nombre = c.getNombre();
        this.slug = c.getSlug();
        this.orden = c.getOrden();
        if (c.getProductos() != null) {
            this.productosCount = c.getProductos().size();
        }
    }
    
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getSlug() { return slug; }
    public Integer getOrden() { return orden; }
    public Integer getProductosCount() { return productosCount; }
}