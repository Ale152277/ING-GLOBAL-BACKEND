package com.ingenieraglobal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingenieraglobal.models.Carrito;
import com.ingenieraglobal.models.DetalleCarrito;
import com.ingenieraglobal.models.Producto;
import com.ingenieraglobal.models.Usuario;
import com.ingenieraglobal.models.enums.EstadoCarritoEnum;
import com.ingenieraglobal.repositories.CarritoRepository;
import com.ingenieraglobal.repositories.DetalleCarritoRepository;
import com.ingenieraglobal.repositories.ProductoRepository;
import com.ingenieraglobal.repositories.UsuarioRepository;
import com.ingenieraglobal.dtos.CarritoDTO;
import com.ingenieraglobal.dtos.request.AgregarAlCarritoRequest;

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
        
        Optional<Carrito> carrito = carritoRepository.findCarritoActivoByUsuario(
            usuarioId, 
            EstadoCarritoEnum.ACTIVO
        );
        
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
        detalleRepository.deleteById(detalleId);
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
