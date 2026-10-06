package com.diego.escuela.dto.datos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Datos resumidos del alumno inscrito")
public record DatosAlumnoInscripcion(
        @Schema(description = "Nombre completo del alumno", example = "Juan Pérez López")
        String nombre,

        @Schema(description = "Matrícula institucional", example = "PELOJU2601")
        String matricula,

        @Schema(description = "Correo institucional", example = "atr.26.juan.perez.lopez.peloju2601@escuela.com.mx")
        String email,

        @Schema(description = "Fecha de ingreso", example = "10/01/2025")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate fechaIngreso
) {
}
