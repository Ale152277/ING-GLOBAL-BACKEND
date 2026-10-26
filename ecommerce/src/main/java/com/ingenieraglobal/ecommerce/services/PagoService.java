package com.ingenieraglobal.ecommerce.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieraglobal.ecommerce.dtos.PagoDTO;
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

@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final VentaRepository ventaRepository;
    private final ComprobanteStorageService comprobanteStorageService;

    public PagoService(
            PagoRepository pagoRepository,
            VentaRepository ventaRepository,
            ComprobanteStorageService comprobanteStorageService) {
        this.pagoRepository = pagoRepository;
        this.ventaRepository = ventaRepository;
        this.comprobanteStorageService = comprobanteStorageService;
    }

    public Pago crearIntentoPago(Venta venta, MetodoPagoEnum metodoPago) {
        if (venta.getEstado() != EstadoVentaEnum.PENDIENTE) {
            throw new ValidationException("No se puede realizar un pago para un pedido que ya no está pendiente");
        }

        if (venta.getEstadoPago() != EstadoPagoEnum.PENDIENTE) {
            throw new ValidationException(
                    "El pedido no se encuentra disponible para realizar un nuevo intento de pago");
        }

        if (pagoRepository.existsByVenta_IdAndEstadoPago(venta.getId(), EstadoPagoEnum.PENDIENTE)) {
            throw new ValidationException("Ya existe un intento de pago pendiente para este pedido");
        }

        if (venta.getFechaExpiracionPago() != null
                && !venta.getFechaExpiracionPago().isAfter(LocalDateTime.now())) {
            throw new ValidationException("El tiempo disponible para realizar el pago ha expirado");
        }

        Pago pago = new Pago();
        pago.setMetodoPago(metodoPago);
        pago.setMonto(venta.getTotal());
        pago.setEstadoPago(EstadoPagoEnum.PENDIENTE);
        pago.setProveedorPago(determinarProveedorPago(metodoPago));

        venta.agregarPago(pago);

        return pagoRepository.save(pago);
    }

    public PagoDTO registrarComprobantePagoManual(
            Long pagoId,
            Long usuarioId,
            MultipartFile archivo) {

        Pago pago = obtenerPagoDelUsuarioEntidad(pagoId, usuarioId);
        validarPagoManualPendiente(pago);

        Venta venta = pago.getVenta();

        if (venta.getEstado() != EstadoVentaEnum.PENDIENTE) {
            throw new ValidationException("El pedido ya no se encuentra pendiente");
        }

        if (venta.getEstadoPago() != EstadoPagoEnum.PENDIENTE) {
            throw new ValidationException("El pedido no se encuentra pendiente de pago");
        }

        if (venta.getFechaExpiracionPago() != null
                && !venta.getFechaExpiracionPago().isAfter(LocalDateTime.now())) {
            throw new ValidationException("El plazo para registrar el comprobante ha expirado");
        }

        String referencia = comprobanteStorageService.guardar(archivo, pago.getId());

        pago.setComprobanteReferencia(referencia);
        pago.setFechaComprobante(LocalDateTime.now());
        pago.setEstadoPago(EstadoPagoEnum.EN_REVISION);

        venta.setEstadoPago(EstadoPagoEnum.EN_REVISION);

        pagoRepository.save(pago);
        ventaRepository.save(venta);

        return new PagoDTO(pago);
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPagoDelUsuario(Long pagoId, Long usuarioId) {
        return new PagoDTO(obtenerPagoDelUsuarioEntidad(pagoId, usuarioId));
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPagosDeVenta(Long ventaId, Long usuarioId) {
        return pagoRepository
                .findByVenta_IdAndVenta_Usuario_IdOrderByFechaCreacionDesc(ventaId, usuarioId)
                .stream()
                .map(PagoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Resource obtenerComprobanteDelUsuario(Long pagoId, Long usuarioId) {
        Pago pago = obtenerPagoDelUsuarioEntidad(pagoId, usuarioId);

        if (pago.getComprobanteReferencia() == null
                || pago.getComprobanteReferencia().isBlank()) {
            throw new RecursoNoEncontradoException("El pago no tiene un comprobante registrado");
        }

        return comprobanteStorageService.cargar(pago.getComprobanteReferencia());
    }

    private ProveedorPagoEnum determinarProveedorPago(MetodoPagoEnum metodoPago) {
        return switch (metodoPago) {
            case YAPE, PLIN, TRANSFERENCIA_DIRECTA -> ProveedorPagoEnum.INTERNO;
            case TARJETA -> throw new ValidationException(
                    "El pago con tarjeta no está disponible actualmente");
        };
    }

    private void validarPagoManualPendiente(Pago pago) {
        if (pago.getProveedorPago() != ProveedorPagoEnum.INTERNO) {
            throw new ValidationException("El pago indicado no corresponde a un pago manual");
        }

        if (!esMetodoPagoManual(pago.getMetodoPago())) {
            throw new ValidationException(
                    "El método de pago indicado no admite comprobante manual");
        }

        if (pago.getEstadoPago() != EstadoPagoEnum.PENDIENTE) {
            throw new ValidationException("El pago no se encuentra pendiente de comprobante");
        }
    }

    private boolean esMetodoPagoManual(MetodoPagoEnum metodoPago) {
        return metodoPago == MetodoPagoEnum.YAPE
                || metodoPago == MetodoPagoEnum.PLIN
                || metodoPago == MetodoPagoEnum.TRANSFERENCIA_DIRECTA;
    }

    private Pago obtenerPagoDelUsuarioEntidad(Long pagoId, Long usuarioId) {
        return pagoRepository
                .findByIdAndVenta_Usuario_Id(pagoId, usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado"));
    }
}
