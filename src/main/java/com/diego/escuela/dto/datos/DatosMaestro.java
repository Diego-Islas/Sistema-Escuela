package com.diego.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos del maestro")
public record DatosMaestro(
        @Schema(description = "Nombre completo del maestro", example = "Laura Martínez Martínez")
        String nombre,

        @Schema(description = "Correo electrónico del maestro", example = "laura.martinez@escuela.com")
        String email,

        @Schema(description = "Teléfono del maestro", example = "5551010789")
        String telefono
) {
}
