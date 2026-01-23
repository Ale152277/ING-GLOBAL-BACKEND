package com.ingenieraglobal.ecommerce.dtos;
import java.math.BigDecimal;

import com.ingenieraglobal.ecommerce.models.Producto;
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String sku;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal precioFinal;
    private Integer descuento;
    private String etiqueta;
    private String imagen;
    private BigDecimal rating;
    private Integer stock;
    private Boolean tieneStock;
    private Long categoriaId;
    private String categoriaNombre;
    private Long marcaId;
    private String marcaNombre;
    
    public ProductoDTO(Producto p) {
        this.id = p.getId();
        this.nombre = p.getNombre();
        this.sku = p.getSku();
        this.descripcion = p.getDescripcion();
        this.precio = p.getPrecio();
        this.precioFinal = p.getPrecioFinal();
        this.descuento = p.getDescuento();
        this.etiqueta = p.getEtiqueta();
        this.imagen = p.getImagen();
        this.rating = p.getRating();
        this.stock = p.getStock();
        this.tieneStock = p.tieneStock();
        this.categoriaId = p.getCategoria().getId();
        this.categoriaNombre = p.getCategoria().getNombre();
        if (p.getMarca() != null) {
            this.marcaId = p.getMarca().getId();
            this.marcaNombre = p.getMarca().getNombre();
        }
    }
    
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getSku() { return sku; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public BigDecimal getPrecioFinal() { return precioFinal; }
    public Integer getDescuento() { return descuento; }
    public String getEtiqueta() { return etiqueta; }
    public String getImagen() { return imagen; }
    public BigDecimal getRating() { return rating; }
    public Integer getStock() { return stock; }
    public Boolean getTieneStock() { return tieneStock; }
    public Long getCategoriaId() { return categoriaId; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public Long getMarcaId() { return marcaId; }
    public String getMarcaNombre() { return marcaNombre; }

    
}
