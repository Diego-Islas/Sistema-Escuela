package com.diego.escuela.dto.grupos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos necesarios para registrar o actualizar un grupo")
public record GrupoRequest(
        @Schema(description = "Identificador del curso", example = "2")
        @NotNull(message = "El identificador del curso es requerido")
        @Positive(message = "El identificador del curso debe ser positivo")
        Long idCurso,

        @Schema(description = "Identificador del maestro", example = "7")
        @NotNull(message = "El identificador del maestro es requerido")
        @Positive(message = "El identificador del maestro debe ser positivo")
        Long idMaestro,

        @Schema(description = "Identificador del aula", example = "3")
        @NotNull(message = "El identificador del aula es requerido")
        @Positive(message = "El identificador del aula debe ser positivo")
        Long idAula,

        @Schema(description = "Periodo del grupo, como YYYY-M o YYYY-MM", example = "2026-01")
        @NotBlank(message = "El periodo es requerido")
        @Pattern(regexp = "^\\d{4}-(0?[1-9]|1[0-2])$", message = "El periodo debe tener formato YYYY-M o YYYY-MM")
        String periodo
) {
}
