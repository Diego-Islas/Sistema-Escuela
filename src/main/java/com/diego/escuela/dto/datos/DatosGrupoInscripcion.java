package com.diego.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos resumidos del grupo de la inscripción")
public record DatosGrupoInscripcion(
        @Schema(description = "Nombre del curso", example = "Matemáticas I")
        String curso,

        @Schema(description = "Nombre completo del maestro", example = "Laura Martínez Martínez")
        String maestro,

        @Schema(description = "Nombre del aula", example = "Aula 101")
        String aula,

        @Schema(description = "Periodo académico", example = "2025-1")
        String periodo
) {
}
