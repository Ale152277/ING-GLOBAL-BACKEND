package com.ingenieraglobal.ecommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.VentaDTO;
import com.ingenieraglobal.ecommerce.exceptions.RecursoNoEncontradoException;
import com.ingenieraglobal.ecommerce.exceptions.ValidationException;
import com.ingenieraglobal.ecommerce.models.Carrito;
import com.ingenieraglobal.ecommerce.models.DetalleCarrito;
import com.ingenieraglobal.ecommerce.models.DetalleVenta;
import com.ingenieraglobal.ecommerce.models.Producto;
import com.ingenieraglobal.ecommerce.models.Venta;
import com.ingenieraglobal.ecommerce.models.enums.EstadoCarritoEnum;
import com.ingenieraglobal.ecommerce.repositories.CarritoRepository;
import com.ingenieraglobal.ecommerce.repositories.ProductoRepository;
import com.ingenieraglobal.ecommerce.repositories.VentaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ingenieraglobal.ecommerce.dtos.PaginaDTO;
import com.ingenieraglobal.ecommerce.models.enums.EstadoVentaEnum;

import com.ingenieraglobal.ecommerce.dtos.CrearVentaRequestDTO;
import com.ingenieraglobal.ecommerce.models.enums.EstadoPagoEnum;
import com.ingenieraglobal.ecommerce.models.enums.MetodoPagoEnum;


@Service
@Transactional
public class VentaService {

        private final VentaRepository ventaRepository;
        private final CarritoRepository carritoRepository;
        private final ProductoRepository productoRepository;
        private final PagoService pagoService;

        public VentaService(
                VentaRepository ventaRepository,
                CarritoRepository carritoRepository,
                ProductoRepository productoRepository,
                PagoService pagoService) {

                this.ventaRepository = ventaRepository;
                this.carritoRepository = carritoRepository;
                this.productoRepository = productoRepository;
                this.pagoService = pagoService;
        }

        private LocalDateTime calcularFechaExpiracion(MetodoPagoEnum metodoPago) {

                LocalDateTime ahora = LocalDateTime.now();

                return switch (metodoPago) {
                        case PLIN, YAPE ->
                                ahora.plusMinutes(30);

                        case TRANSFERENCIA_DIRECTA ->
                                ahora.plusHours(24);

                        case TARJETA ->
                                throw new ValidationException("El pago con tarjeta no está disponible actualmente");


                };

        }

        public VentaDTO crearVenta(Long usuarioId, CrearVentaRequestDTO request) {

                Carrito carrito = carritoRepository.findCarritoActivoByUsuario(usuarioId, EstadoCarritoEnum.ACTIVO)
                                .orElseThrow(() -> new RecursoNoEncontradoException("No tienes un carrito activo"));

                if (carrito.getDetalles() == null || carrito.getDetalles().isEmpty()) {
                        throw new ValidationException("No puedes confirmar una compra con el carrito vacío");
                }

                if (ventaRepository.existsByCarritoId(carrito.getId())) {
                        throw new ValidationException("Este carrito ya fue convertido en una venta");
                }

                Venta venta = new Venta();
                venta.setUsuario(carrito.getUsuario());
                venta.setCarrito(carrito);
                venta.setTotal(carrito.getTotal());

                venta.setNombreReceptor(request.getNombreReceptor().trim());
                venta.setTelefonoEntrega(request.getTelefonoEntrega().trim());
                venta.setDireccionEntrega(request.getDireccionEntrega().trim());

                if (request.getReferenciaEntrega() != null) {
                        venta.setReferenciaEntrega(request.getReferenciaEntrega().trim());
                }

                venta.setMetodoPago(request.getMetodoPago());
                venta.setEstadoPago(EstadoPagoEnum.PENDIENTE);
                venta.setFechaExpiracionPago(calcularFechaExpiracion(request.getMetodoPago()));

                for (DetalleCarrito detalleCarrito : carrito.getDetalles()) {

                        Producto producto = detalleCarrito.getProducto();
                        if (producto == null) {
                                throw new ValidationException("El carrito contiene una presentación no soportada en el checkout actual");
                        }

                        if (producto.getStock() < detalleCarrito.getCantidad()) {
                                throw new ValidationException("Stock insuficiente para el producto: " + producto.getNombre());
                        }

                        DetalleVenta detalleVenta = new DetalleVenta(venta, detalleCarrito);
                        venta.agregarDetalle(detalleVenta);
                        producto.setStock(producto.getStock() - detalleCarrito.getCantidad());
                        productoRepository.save(producto);
                }

                Venta ventaGuardada = ventaRepository.save(venta);
                pagoService.crearIntentoPago(ventaGuardada, request.getMetodoPago());
                carrito.setEstado(EstadoCarritoEnum.INACTIVO);
                carritoRepository.save(carrito);
                return new VentaDTO(ventaGuardada);
        }

        @Transactional(readOnly = true)

        public PaginaDTO<VentaDTO> obtenerVentasDelUsuario(
                        Long usuarioId,
                        int page,
                        int size,
                        EstadoVentaEnum estado,
                        LocalDate fechaDesde,
                        LocalDate fechaHasta,
                        BigDecimal precioMin,
                        BigDecimal precioMax) {
                Pageable pageable = PageRequest.of(page, size);

                LocalDateTime fechaDesdeInicio = fechaDesde != null
                                ? fechaDesde.atStartOfDay()
                                : null;
                LocalDateTime fechaHastaExclusiva = fechaHasta != null
                                ? fechaHasta.plusDays(1).atStartOfDay()
                                : null;

                Page<Venta> paginaVentas = ventaRepository.buscarVentasDelUsuario(
                                usuarioId,
                                estado,
                                fechaDesdeInicio,
                                fechaHastaExclusiva,
                                precioMin,
                                precioMax,
                                pageable);

                List<VentaDTO> ventas = paginaVentas
                                .getContent()
                                .stream()
                                .map(VentaDTO::new)
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
}