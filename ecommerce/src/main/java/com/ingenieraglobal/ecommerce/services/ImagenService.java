package com.ingenieraglobal.ecommerce.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class ImagenService {
    
    @Autowired
    private Cloudinary cloudinary;

    private static final long MAX_SIZE = 5 * 1024 * 1024;

    private static final String[] TIPOS_PERMITIDOS = {"image/jpeg", "image/jpg", "image/png"};


    public String subirImagen(MultipartFile archivo){
        validarArchivo(archivo);

         try {
            Map<String, Object> opciones = ObjectUtils.asMap(
                    "folder", "ingenieria-global/productos",
                    "public_id", UUID.randomUUID().toString(),
                    "resource_type", "image"
            );

            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(),opciones);
            return uploadResult.get("secure_url").toString();



        }catch (IOException e){
            throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
        } 
    }

    private void validarArchivo(MultipartFile archivo){
        if(archivo == null || archivo.isEmpty()){
            throw new RuntimeException("El archivo está vacio");


        }
        if(archivo.getSize()>MAX_SIZE){
            throw new RuntimeException("La imagen no puede pesar mas de 5MB");
        }

        String tipo = archivo.getContentType();
        boolean valido = false;
        for (String t : TIPOS_PERMITIDOS){
            if(t.equalsIgnoreCase(tipo)){valido = true; break;}
        }
        if(!valido){
            throw new RuntimeException("El tipo de archivo no está permitido, solo jpg, jpeg y png");
        }
    }
}
