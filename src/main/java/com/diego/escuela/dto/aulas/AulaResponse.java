package com.diego.escuela.dto.aulas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta de un aula")
public record AulaResponse(
        @Schema(description = "Identificador del aula", example = "1")
        Long id,

        @Schema(description = "Nombre del aula", example = "Aula 101")
        String nombre,

        @Schema(description = "Capacidad máxima del aula", example = "30")
        Integer capacidad
) {
}
