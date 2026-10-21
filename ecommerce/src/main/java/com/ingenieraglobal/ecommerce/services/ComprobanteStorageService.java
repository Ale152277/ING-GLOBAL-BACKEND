package com.ingenieraglobal.ecommerce.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieraglobal.ecommerce.exceptions.ValidationException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.ingenieraglobal.ecommerce.exceptions.RecursoNoEncontradoException;

@Service
public class ComprobanteStorageService {
    private static final long TAMANO_MAXIMO = 5 * 1024 * 1024;

    private final Path directorioBase;

    public ComprobanteStorageService(
            @Value("${app.upload.comprobantes-dir:uploads/comprobantes}") String directorio) {
        this.directorioBase = Path.of(directorio).toAbsolutePath().normalize();
    }

    public String guardar(MultipartFile archivo, Long pagoId) {

        validarArchivo(archivo);

        try {
            Path carpetaPago = directorioBase.resolve("pago-" + pagoId).normalize();

            Files.createDirectories(carpetaPago);

            String nombreArchivo = UUID.randomUUID() + obtenerExtension(archivo);

            Path destino = carpetaPago.resolve(nombreArchivo).normalize();

            if (!destino.startsWith(directorioBase)) {
                throw new ValidationException("Ruta de comprobante no valida");
            }

            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return directorioBase.relativize(destino).toString();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo almacenar el comprobante", e);
        }
    }

    private void validarArchivo(MultipartFile archivo) {

        if (archivo == null || archivo.isEmpty()) {
            throw new ValidationException("Debes adjuntar un comprobante");
        }

        if (archivo.getSize() > TAMANO_MAXIMO) {
            throw new ValidationException("El comprobante no puede superar los 5 MB");
        }

        String tipo = archivo.getContentType();

        if (!"image/jpeg".equals(tipo) && !"image/png".equals(tipo) && !"application/pdf".equals(tipo)) {

            throw new ValidationException("El comprobante debe ser JPG, PNG o PDF");
        }
    }

    private String obtenerExtension(MultipartFile archivo) {

        return switch (archivo.getContentType()) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "application/pdf" -> ".pdf";

            default -> throw new ValidationException("Formato de comprobante no permitido");
        };
    }

    public Resource cargar(String referencia) {
        if (referencia == null || referencia.isBlank()) {
            throw new RecursoNoEncontradoException("Comprobante no encontrado");
        }
        try {
            Path archivo = directorioBase.resolve(referencia).normalize();

            if (!archivo.startsWith(directorioBase)) {
                throw new ValidationException("Referencia de comprobante no valida");
            }

            Resource recurso = new UrlResource(archivo.toUri());

            if (!recurso.exists() || !recurso.isReadable()) {
                throw new RecursoNoEncontradoException("Comprobante no encontrado");
            }
            return recurso;

        } catch (MalformedURLException e) {
            throw new IllegalStateException( "No se pudo leer el comprobante", e);
        }
    }

}
