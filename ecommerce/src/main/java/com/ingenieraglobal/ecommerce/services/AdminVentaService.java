package com.ingenieraglobal.ecommerce.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.PaginaDTO;
import com.ingenieraglobal.ecommerce.dtos.admin.AdminVentaResumenDTO;
import com.ingenieraglobal.ecommerce.models.Venta;
import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;
import com.ingenieraglobal.ecommerce.repositories.ProductoRepository;
import com.ingenieraglobal.ecommerce.repositories.VentaRepository;

import com.ingenieraglobal.ecommerce.exceptions.RecursoNoEncontradoException;
import com.ingenieraglobal.ecommerce.exceptions.ValidationException;

import com.ingenieraglobal.ecommerce.dtos.AdminVentaDetalleDTO;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;

@Service
@Transactional(readOnly = true)
public class AdminVentaService {

    private final StockService stockService;

    private final VentaRepository ventaRepository;

    public AdminVentaService(VentaRepository ventaRepository, ProductoRepository productoRepository,
            StockService stockService) {
        this.ventaRepository = ventaRepository;
        this.stockService = stockService;
    }

    public PaginaDTO<AdminVentaResumenDTO> obtenerVentas(
            int page,
            int size,
            EstadoVentaEnum estado) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Venta> paginaVentas;

        if (estado != null) {
            paginaVentas = ventaRepository.findByEstadoOrderByFechaVentaDesc(estado, pageable);

        } else {

            paginaVentas = ventaRepository.findAllByOrderByFechaVentaDesc(pageable);

        }
        List<AdminVentaResumenDTO> ventas = paginaVentas
                .getContent()
                .stream()
                .map(AdminVentaResumenDTO::new)
                .toList();

        return new PaginaDTO<>(
                ventas,
                paginaVentas.getNumber(),
                paginaVentas.getSize(),
                paginaVentas.getTotalElements(),
                paginaVentas.getTotalPages(),
                paginaVentas.isFirst(),
                paginaVentas.isLast());

    }

    public AdminVentaDetalleDTO obtenerVentaPorId(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId).orElseThrow(
                () -> new RecursoNoEncontradoException("PEDIDO NO ENCONTRADO"));
        return new AdminVentaDetalleDTO(venta);
    }

    @Transactional
    public AdminVentaResumenDTO actualizarEstado(Long ventaId, EstadoVentaEnum nuevoEstado) {
        Venta venta = ventaRepository.findById(ventaId).orElseThrow(
                () -> new RecursoNoEncontradoException("Pedido no encontrado"));

        if (nuevoEstado == null) {
            throw new ValidationException("Debes indicar el nuevo estado del pedido");

        }

        EstadoVentaEnum estadoActual = venta.getEstado();

        validarTransicionEstado(estadoActual, nuevoEstado);

        validarEstadoPagoParaCambio(venta, nuevoEstado);

        if (nuevoEstado == EstadoVentaEnum.CANCELADA) {
            stockService.restaurarStock(venta);
        }

        venta.setEstado(nuevoEstado);

        Venta ventaActualizada = ventaRepository.save(venta);

        return new AdminVentaResumenDTO(
                ventaActualizada);

    }

    private void validarTransicionEstado(
            EstadoVentaEnum estadoActual,
            EstadoVentaEnum nuevoEstado) {

        boolean transicionValida = switch (estadoActual) {

            case PENDIENTE ->
                nuevoEstado == EstadoVentaEnum.CONFIRMADA
                        || nuevoEstado == EstadoVentaEnum.CANCELADA;

            case CONFIRMADA ->
                nuevoEstado == EstadoVentaEnum.COMPLETA
                        || nuevoEstado == EstadoVentaEnum.CANCELADA;

            case COMPLETA, CANCELADA -> false;
        };

        if (!transicionValida) {
            throw new ValidationException(
                    "No se puede cambiar el pedido de " + estadoActual + " a " + nuevoEstado);
        }

    }

    private void validarEstadoPagoParaCambio(Venta venta, EstadoVentaEnum nuevoEstado) {

        if (nuevoEstado == EstadoVentaEnum.CONFIRMADA && venta.getEstadoPago() != EstadoPagoEnum.PAGADO) {
            throw new ValidationException("No se puede confirmar el pedido porque el pago aún no ha sido confirmado");
        }

        if (nuevoEstado == EstadoVentaEnum.CANCELADA && venta.getEstadoPago() == EstadoPagoEnum.PAGADO) {
            throw new ValidationException("No se puede cancelar un pedido pagado sin procesar primero el reembolso");
        }
    }

}
