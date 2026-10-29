package com.ingenieraglobal.ecommerce.dtos;

public record DireccionDTO(
    Long id,
    String alias,
    String nombreReceptor,
    String telefono,
    String departamento,
    String provincia,
    String distrito,
    String direccion,
    String referencia,
    String codigoPostal,
    boolean predeterminada
) {}