package com.diego.escuela.dto.maestros;

import com.diego.escuela.dto.datos.DatosCurso;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Datos de respuesta de un maestro")
public record MaestroResponse(

        @Schema(
                description = "Identificador del maestro",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nombre del maestro",
                example = "Maximo"
        )
        String nombre,

        @Schema(
                description = "Correo electrónico del maestro",
                example = "maximo.hernandez@escuela.com"
        )
        String email,

        @Schema(
                description = "Teléfono del maestro",
                example = "7711234567"
        )
        String telefono,

        @Schema(description = "Datos de los cursos del maestro")
        List<DatosCurso> cursos) {
}