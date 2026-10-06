package com.diego.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Curso inscrito y calificación del alumno")
public record DatosCalificacionAlumno(
        @Schema(description = "Nombre del curso", example = "Matemáticas I")
        String curso,

        @Schema(description = "Periodo académico", example = "2025-1")
        String periodo,

        @Schema(description = "Calificación del curso; null si aún no está registrada",
                example = "8.00", nullable = true)
        BigDecimal calificacion
) {
}
