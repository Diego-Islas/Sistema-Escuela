package com.diego.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos del curso")
public record DatosCurso(
        @Schema(
                description = "Nombre del curso",
                example = "Matemáticas 1"
        )
        String nombre,

        @Schema(
                description = "Descripción del curso",
                example = "Curso de calculo integral"
        )
        String descripcion,

        @Schema(
                description = "Créditos del curso",
                example = "5"
        )

        Integer creditos
) {
}
