package com.diego.escuela.dto.calificaciones;

import com.diego.escuela.dto.datos.DatosInscripcionCalificacion;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos de respuesta de una calificación")
public record CalificacionResponse(
        @Schema(description = "Identificador de la calificación", example = "2")
        Long id,

        @Schema(description = "Inscripción a la que pertenece la calificación")
        DatosInscripcionCalificacion inscripcion,

        @Schema(description = "Calificación del alumno", example = "8.5")
        BigDecimal calificacion,

        @Schema(description = "Fecha de registro", example = "11/02/2026")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate fechaRegistro
) {
}
