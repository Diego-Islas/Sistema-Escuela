package com.diego.escuela.dto.calificaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Datos necesarios para registrar o actualizar una calificación")
public record CalificacionRequest(
        @Schema(description = "Identificador de la inscripción", example = "15")
        @NotNull(message = "El identificador de la inscripción es requerido")
        @Positive(message = "El identificador de la inscripción debe ser positivo")
        Long idInscripcion,

        @Schema(description = "Calificación entre 0 y 10, con un decimal", example = "8.5")
        @NotNull(message = "La calificación es requerida")
        @DecimalMin(value = "0.0", message = "La calificación mínima es 0")
        @DecimalMax(value = "10.0", message = "La calificación máxima es 10")
        @Digits(integer = 2, fraction = 1, message = "La calificación admite como máximo un decimal")
        BigDecimal calificacion
) {
}
