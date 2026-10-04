package com.ingenieraglobal.ecommerce.services;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.admin.AdminDashboardDTO;
import com.ingenieraglobal.ecommerce.dtos.admin.PedidoMesDTO;
import com.ingenieraglobal.ecommerce.dtos.admin.PedidoRecienteDTO;
import com.ingenieraglobal.ecommerce.dtos.admin.ProductoStockBajoDTO;
import com.ingenieraglobal.ecommerce.models.Venta;
import com.ingenieraglobal.ecommerce.models.enums.EstadoEnum;
import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;
import com.ingenieraglobal.ecommerce.repositories.ProductoRepository;
import com.ingenieraglobal.ecommerce.repositories.VentaRepository;

@Service
@Transactional(readOnly = true)
public class AdminDashboardService {

    private static final int UMBRAL_STOCK_BAJO = 10;

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public AdminDashboardService(
            VentaRepository ventaRepository,
            ProductoRepository productoRepository) {

        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    public AdminDashboardDTO obtenerDashboard() {
        long pedidosTotales = ventaRepository.count();

        long pedidosPendientes = ventaRepository.countByEstado(EstadoVentaEnum.PENDIENTE);
        long pedidosCompletados = ventaRepository.countByEstado(EstadoVentaEnum.COMPLETA);
        long productosActivos = productoRepository.countByEstado(EstadoEnum.ACTIVO);

        Long productosStockBajo = productoRepository.countByEstadoAndStockLessThanEqual(EstadoEnum.ACTIVO,
                UMBRAL_STOCK_BAJO);

        List<PedidoMesDTO> pedidosPorMes = obtenerPedidosPorMes();

        List<PedidoRecienteDTO> pedidosRecientes = ventaRepository
                .findTop5ByOrderByFechaVentaDesc()
                .stream()
                .map(PedidoRecienteDTO::new)
                .toList();

        List<ProductoStockBajoDTO> productosConStockBajo = productoRepository
                .findTop5ByEstadoAndStockLessThanEqualOrderByStockAsc(
                        EstadoEnum.ACTIVO,
                        UMBRAL_STOCK_BAJO)
                .stream()
                .map(ProductoStockBajoDTO::new)
                .toList();

        return new AdminDashboardDTO(
                pedidosTotales,
                pedidosPendientes,
                pedidosCompletados,
                productosActivos,
                productosStockBajo,
                pedidosPorMes,
                pedidosRecientes,
                productosConStockBajo);
    }

    private List<PedidoMesDTO> obtenerPedidosPorMes() {

        YearMonth mesActual = YearMonth.now();

        YearMonth primerMes = mesActual.minusMonths(5);

        LocalDateTime fechaDesde = primerMes
                .atDay(1)
                .atStartOfDay();

        List<Venta> ventas = ventaRepository
                .findByFechaVentaGreaterThanEqualOrderByFechaVentaAsc(
                        fechaDesde);

        Map<YearMonth, Long> ventasPorMes = new LinkedHashMap<>();

        for (int i = 0; i < 6; i++) {

            YearMonth mes = primerMes.plusMonths(i);

            ventasPorMes.put(mes, 0L);
        }

        for (Venta venta : ventas) {

            YearMonth mesVenta = YearMonth.from(
                    venta.getFechaVenta());

            if (ventasPorMes.containsKey(mesVenta)) {

                long cantidadActual = ventasPorMes.get(mesVenta);

                ventasPorMes.put(
                        mesVenta,
                        cantidadActual + 1);
            }
        }

        return ventasPorMes
                .entrySet()
                .stream()
                .map(entry -> new PedidoMesDTO(
                        formatearMes(entry.getKey()),
                        entry.getValue()))
                .toList();
    }

    private String formatearMes(YearMonth mes) {

        String nombreMes = mes.getMonth()
                .getDisplayName(
                        TextStyle.SHORT,
                        Locale.forLanguageTag("es-PE"));

        return nombreMes
                .substring(0, 1)
                .toUpperCase()
                + nombreMes.substring(1);
    }

}
