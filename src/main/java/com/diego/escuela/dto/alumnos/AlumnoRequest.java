package com.diego.escuela.dto.alumnos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos personales necesarios para registrar o actualizar un alumno")
public record AlumnoRequest(
        @Schema(description = "Nombre del alumno", example = "Carlos")
        @NotBlank(message = "El nombre es requerido")
        @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
        String nombre,

        @Schema(description = "Apellido paterno del alumno", example = "González")
        @NotBlank(message = "El apellido paterno es requerido")
        @Size(max = 50, message = "El apellido paterno no debe superar los 50 caracteres")
        String apellidoPaterno,

        @Schema(description = "Apellido materno del alumno", example = "Ramírez")
        @NotBlank(message = "El apellido materno es requerido")
        @Size(max = 50, message = "El apellido materno no debe superar los 50 caracteres")
        String apellidoMaterno
) {
}
