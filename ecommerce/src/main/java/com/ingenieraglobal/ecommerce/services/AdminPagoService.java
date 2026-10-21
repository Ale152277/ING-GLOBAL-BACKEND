package com.ingenieraglobal.ecommerce.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.AdminPagoDTO;
import com.ingenieraglobal.ecommerce.exceptions.RecursoNoEncontradoException;
import com.ingenieraglobal.ecommerce.exceptions.ValidationException;
import com.ingenieraglobal.ecommerce.models.Pago;
import com.ingenieraglobal.ecommerce.models.Venta;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;
import com.ingenieraglobal.ecommerce.models.enums.MetodoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.ProveedorPagoEnum;
import com.ingenieraglobal.ecommerce.repositories.PagoRepository;
import com.ingenieraglobal.ecommerce.repositories.VentaRepository;
import org.springframework.core.io.Resource;

@Service
@Transactional
public class AdminPagoService {

    private final PagoRepository pagoRepository;
    private final VentaRepository ventaRepository;
    private final ComprobanteStorageService comprobanteStorageService;

    public AdminPagoService(PagoRepository pagoRepository, VentaRepository ventaRepository,
            ComprobanteStorageService comprobanteStorageService) {
        this.pagoRepository = pagoRepository;
        this.ventaRepository = ventaRepository;
        this.comprobanteStorageService = comprobanteStorageService;
    }

    @Transactional(readOnly = true)
    public List<AdminPagoDTO> obtenerPagosEnRevision() {

        return pagoRepository.findByEstadoPagoOrderByFechaComprobanteAsc(EstadoPagoEnum.EN_REVISION)
                .stream()
                .map(AdminPagoDTO::new)
                .toList();
    }

    public AdminPagoDTO aprobarPagoManual(Long pagoId) {

        Pago pago = obtenerPago(pagoId);

        validarPagoManualEnRevision(pago);

        pago.setEstadoPago(EstadoPagoEnum.PAGADO);
        pago.setFechaPago(LocalDateTime.now());

        Venta venta = pago.getVenta();

        venta.setEstadoPago(EstadoPagoEnum.PAGADO);
        venta.setMetodoPago(pago.getMetodoPago());

        pagoRepository.save(pago);
        ventaRepository.save(venta);

        return new AdminPagoDTO(pago);
    }

    public AdminPagoDTO rechazarPagoManual(Long pagoId) {

        Pago pago = obtenerPago(pagoId);

        validarPagoManualEnRevision(pago);

        pago.setEstadoPago(EstadoPagoEnum.RECHAZADO);

        Venta venta = pago.getVenta();

        venta.setEstadoPago(EstadoPagoEnum.PENDIENTE);

        pagoRepository.save(pago);
        ventaRepository.save(venta);

        return new AdminPagoDTO(pago);
    }

    private Pago obtenerPago(Long pagoId) {

        return pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Pago no encontrado"));
    }

    private void validarPagoManualEnRevision(Pago pago) {

        if (pago.getProveedorPago() != ProveedorPagoEnum.INTERNO) {
            throw new ValidationException("El pago indicado no corresponde a una validación interna");
        }

        if (!esMetodoPagoManual(pago.getMetodoPago())) {
            throw new ValidationException("El método indicado no corresponde a un pago manual");
        }

        if (pago.getEstadoPago() != EstadoPagoEnum.EN_REVISION) {
            throw new ValidationException("El pago no se encuentra pendiente de revisión");
        }

        Venta venta = pago.getVenta();

        if (venta.getEstado() != EstadoVentaEnum.PENDIENTE) {
            throw new ValidationException("El pedido ya no se encuentra pendiente");
        }

        if (venta.getEstadoPago() != EstadoPagoEnum.EN_REVISION) {
            throw new ValidationException("El pedido no se encuentra pendiente de revisión de pago");
        }
    }

    private boolean esMetodoPagoManual(MetodoPagoEnum metodoPago) {

        return metodoPago == MetodoPagoEnum.YAPE
                || metodoPago == MetodoPagoEnum.PLIN
                || metodoPago == MetodoPagoEnum.TRANSFERENCIA_DIRECTA;
    }

    @Transactional(readOnly = true)
    public Resource obtenerComprobante(Long pagoId) {
        Pago pago = obtenerPago(pagoId);

        if (pago.getComprobanteReferencia() == null || pago.getComprobanteReferencia().isBlank()) {
            throw new RecursoNoEncontradoException("El pago no tiene un comprobante registrado");
        }

        return comprobanteStorageService.cargar(
                pago.getComprobanteReferencia());
    }
}