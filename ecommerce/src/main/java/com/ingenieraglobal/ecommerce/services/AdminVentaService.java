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
import com.ingenieraglobal.ecommerce.models.DetalleVenta;
import com.ingenieraglobal.ecommerce.models.Producto;
import com.ingenieraglobal.ecommerce.repositories.ProductoRepository;

@Service
@Transactional(readOnly = true)
public class AdminVentaService {

    private final VentaRepository ventaRepository;

    private ProductoRepository productoRepository;

    public AdminVentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
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

    @Transactional
    public AdminVentaResumenDTO actualizarEstado(Long ventaId, EstadoVentaEnum nuevoEstado) {
        Venta venta = ventaRepository.findById(ventaId).orElseThrow(
                () -> new RecursoNoEncontradoException("Pedido no encontrado"));

        if (nuevoEstado == null) {
            throw new ValidationException("Debes indicar el nuevo estado del pedido");

        }

        EstadoVentaEnum estadoActual = venta.getEstado();

        validarTransicionEstado(
                estadoActual,
                nuevoEstado);

        if (nuevoEstado == EstadoVentaEnum.CANCELADA) {
            restaurarStock(venta);
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
                    "No se puede cambiar el pedido de "
                            + estadoActual
                            + " a "
                            + nuevoEstado);
        }

    }

    private void restaurarStock(Venta venta) {

        for (DetalleVenta detalle : venta.getDetalles()) {

            Producto producto = detalle.getProducto();

            producto.setStock(
                    producto.getStock()
                            + detalle.getCantidad());

            productoRepository.save(producto);
        }
    }

}
