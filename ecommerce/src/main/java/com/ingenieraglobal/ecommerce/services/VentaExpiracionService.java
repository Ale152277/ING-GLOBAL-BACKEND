package com.ingenieraglobal.ecommerce.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.models.Pago;
import com.ingenieraglobal.ecommerce.models.Venta;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;
import com.ingenieraglobal.ecommerce.repositories.PagoRepository;
import com.ingenieraglobal.ecommerce.repositories.ProductoRepository;
import com.ingenieraglobal.ecommerce.repositories.VentaRepository;

@Service 
public class VentaExpiracionService {

    private final StockService stockService;
    private final VentaRepository ventaRepository;
    private final PagoRepository pagoRepository;

    public VentaExpiracionService(
        VentaRepository ventaRepository,
        ProductoRepository productoRepository,
        PagoRepository pagoRepository, StockService stockService
    ){
        this.ventaRepository = ventaRepository;
        this.pagoRepository = pagoRepository;
        this.stockService = stockService;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional 
    public void expirarVentasPendientes(){
        LocalDateTime ahora = LocalDateTime.now();

        List<Venta> ventasExpiradas = ventaRepository
        .findByEstadoAndEstadoPagoAndFechaExpiracionPagoLessThanEqual(
            EstadoVentaEnum.PENDIENTE,
            EstadoPagoEnum.PENDIENTE,
            ahora);
        
        
        for(Venta venta : ventasExpiradas){
            stockService.restaurarStock(venta);
            expirarPagosPendientes(venta);
            venta.setEstadoPago(EstadoPagoEnum.EXPIRADO);
            venta.setEstado(EstadoVentaEnum.CANCELADA);
            ventaRepository.save(venta);
        }
    }

    private void expirarPagosPendientes(Venta venta) {

        for (Pago pago : venta.getPagos()) {

            if (pago.getEstadoPago() == EstadoPagoEnum.PENDIENTE) {
                pago.setEstadoPago(EstadoPagoEnum.EXPIRADO);
                pagoRepository.save(pago);
            }
        }
    }
}
