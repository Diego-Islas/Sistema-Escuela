package com.diego.escuela.dto.inscripciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos necesarios para inscribir a un alumno en un grupo")
public record InscripcionRequest(
        @Schema(description = "Identificador del alumno", example = "10")
        @NotNull(message = "El identificador del alumno es requerido")
        @Positive(message = "El identificador del alumno debe ser positivo")
        Long idAlumno,

        @Schema(description = "Identificador del grupo", example = "5")
        @NotNull(message = "El identificador del grupo es requerido")
        @Positive(message = "El identificador del grupo debe ser positivo")
        Long idGrupo
) {
}
