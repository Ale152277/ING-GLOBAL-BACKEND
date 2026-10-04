package com.ingenieraglobal.ecommerce.dtos.admin;

import java.util.List;

public class AdminDashboardDTO {

    private Long pedidosTotales;
    private Long pedidosPendientes;
    private Long pedidosCompletados;

    private Long productosActivos;
    private Long productosStockBajo;

    private List<PedidoMesDTO> pedidosPorMes;

    private List<PedidoRecienteDTO> pedidosRecientes;

    private List<ProductoStockBajoDTO> productosConStockBajo;

    public AdminDashboardDTO(
            Long pedidosTotales,
            Long pedidosPendientes,
            Long pedidosCompletados,
            Long productosActivos,
            Long productosStockBajo,
            List<PedidoMesDTO> pedidosPorMes,
            List<PedidoRecienteDTO> pedidosRecientes,
            List<ProductoStockBajoDTO> productosConStockBajo) {

        this.pedidosTotales = pedidosTotales;
        this.pedidosPendientes = pedidosPendientes;
        this.pedidosCompletados = pedidosCompletados;

        this.productosActivos = productosActivos;
        this.productosStockBajo = productosStockBajo;

        this.pedidosPorMes = pedidosPorMes;

        this.pedidosRecientes = pedidosRecientes;

        this.productosConStockBajo = productosConStockBajo;
    }

    public Long getPedidosTotales() {
        return pedidosTotales;
    }

    public Long getPedidosPendientes() {
        return pedidosPendientes;
    }

    public Long getPedidosCompletados() {
        return pedidosCompletados;
    }

    public Long getProductosActivos() {
        return productosActivos;
    }

    public Long getProductosStockBajo() {
        return productosStockBajo;
    }

    public List<PedidoMesDTO> getPedidosPorMes() {
        return pedidosPorMes;
    }

    public List<PedidoRecienteDTO> getPedidosRecientes() {
        return pedidosRecientes;
    }

    public List<ProductoStockBajoDTO> getProductosConStockBajo() {
        return productosConStockBajo;
    }
}