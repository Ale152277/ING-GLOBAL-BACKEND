package com.ingenieraglobal.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.ecommerce.dtos.CarritoDTO;
import com.ingenieraglobal.ecommerce.dtos.request.AgregarAlCarritoRequest;
import com.ingenieraglobal.ecommerce.models.Carrito;
import com.ingenieraglobal.ecommerce.models.DetalleCarrito;
import com.ingenieraglobal.ecommerce.models.Producto;
import com.ingenieraglobal.ecommerce.models.Usuario;
import com.ingenieraglobal.ecommerce.models.enums.EstadoCarritoEnum;
import com.ingenieraglobal.ecommerce.repositories.CarritoRepository;
import com.ingenieraglobal.ecommerce.repositories.DetalleCarritoRepository;
import com.ingenieraglobal.ecommerce.repositories.ProductoRepository;
import com.ingenieraglobal.ecommerce.repositories.UsuarioRepository;

import java.time.LocalDateTime;

import java.util.Optional;

@Service
@Transactional

public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DetalleCarritoRepository detalleRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public CarritoDTO obtenerCarritoActivo(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Optional<Carrito> carrito = carritoRepository.findCarritoActivoByUsuario(usuarioId, EstadoCarritoEnum.ACTIVO);
        
        if (carrito.isEmpty()) {
            Carrito nuevoCarrito = new Carrito(usuario);
            nuevoCarrito = carritoRepository.save(nuevoCarrito);
            return new CarritoDTO(nuevoCarrito);
        }
        
        return new CarritoDTO(carrito.get());
    }
    
    public CarritoDTO agregarProducto(Long usuarioId, AgregarAlCarritoRequest request) {
        Carrito carrito = carritoRepository.findCarritoActivoByUsuario(
            usuarioId,
            EstadoCarritoEnum.ACTIVO
        ).orElseGet(() -> {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            return carritoRepository.save(new Carrito(usuario));
        });
        
        Producto producto = productoRepository.findById(request.getProductoId())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Optional<DetalleCarrito> detalle = detalleRepository.findByCarritoIdAndProductoId(
            carrito.getId(),
            request.getProductoId()
        );
        
        if (detalle.isPresent()) {
            DetalleCarrito dc = detalle.get();
            dc.setCantidad(dc.getCantidad() + request.getCantidad());
            detalleRepository.save(dc);
        } else {
            DetalleCarrito nuevoDetalle = new DetalleCarrito(carrito, producto, request.getCantidad());
            detalleRepository.save(nuevoDetalle);
        }
        
        carrito = carritoRepository.findById(carrito.getId()).get();
        return new CarritoDTO(carrito);
    }
    
    public void eliminarProducto(Long carritoId, Long detalleId) {
    DetalleCarrito detalle = detalleRepository
            .findByIdAndCarritoId(detalleId, carritoId)
            .orElseThrow(() -> new RuntimeException("Detalle no encontrado en el carrito"));

    detalleRepository.delete(detalle);
    }
    
    public CarritoDTO enviarAWhatsapp(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        
        carrito.setEstado(EstadoCarritoEnum.ENVIADO);
        carrito.setFechaEnvioWhatsapp(LocalDateTime.now());
        carrito = carritoRepository.save(carrito);
        
        return new CarritoDTO(carrito);
    }
    
    public void vaciarCarrito(Long carritoId) {
        detalleRepository.deleteByCarritoId(carritoId);
    }

}
