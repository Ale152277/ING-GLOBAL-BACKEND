package com.ingenieraglobal.ecommerce.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DireccionRequest(

    @NotBlank(message = "El alias es obligatorio")
    @Size(max = 50)
    String alias,

    @NotBlank(message = "El nombre del receptor es obligatorio")
    @Size(max = 100)
    String nombreReceptor,

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "[0-9]{9}", message = "El teléfono debe tener 9 dígitos")
    String telefono,

    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 100)
    String departamento,

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 100)
    String provincia,

    @NotBlank(message = "El distrito es obligatorio")
    @Size(max = 100)
    String distrito,

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255)
    String direccion,

    @Size(max = 255)
    String referencia,

    @Pattern(
        regexp = "^$|[0-9]{5}",
        message = "El código postal debe tener 5 dígitos"
    )
    String codigoPostal,

    Boolean predeterminada

) {}