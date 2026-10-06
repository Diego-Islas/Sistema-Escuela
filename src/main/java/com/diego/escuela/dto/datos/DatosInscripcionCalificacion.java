package com.diego.escuela.dto.datos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Datos de la inscripción asociada a una calificación")
public record DatosInscripcionCalificacion(
        @Schema(description = "Datos del alumno")
        DatosAlumnoInscripcion alumno,

        @Schema(description = "Datos del grupo")
        DatosGrupoInscripcion grupo,

        @Schema(description = "Fecha en que se realizó la inscripción", example = "11/02/2026")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate fechaInscripcion
) {
}
