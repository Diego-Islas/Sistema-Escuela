package com.diego.escuela.dto.cursos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar un curso")
public record CursoRequest(
        @Schema(description = "Nombre único del curso", example = "Matemáticas I")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 100, message = "El nombre debe tener entre 5 y 100 caracteres")
        String nombre,

        @Schema(description = "Descripción del contenido del curso", example = "Fundamentos matemáticos para nivel básico")
        @NotBlank(message = "La descripción es requerida")
        @Size(max = 200, message = "La descripción no debe superar los 200 caracteres")
        String descripcion,

        @Schema(description = "Cantidad de créditos del curso", example = "6")
        @NotNull(message = "Los créditos son requeridos")
        @Positive(message = "Los créditos deben ser positivos")
        Integer creditos
) {
}
