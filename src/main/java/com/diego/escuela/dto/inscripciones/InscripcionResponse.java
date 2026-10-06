package com.diego.escuela.dto.inscripciones;

import com.diego.escuela.dto.datos.DatosAlumnoInscripcion;
import com.diego.escuela.dto.datos.DatosGrupoInscripcion;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos de respuesta de una inscripción")
public record InscripcionResponse(
        @Schema(description = "Identificador de la inscripción", example = "1")
        Long id,

        @Schema(description = "Datos del alumno inscrito")
        DatosAlumnoInscripcion alumno,

        @Schema(description = "Datos del grupo al que se inscribió")
        DatosGrupoInscripcion grupo,

        @Schema(description = "Calificación; null si todavía no se ha registrado",
                example = "8.0", nullable = true)
        BigDecimal calificacion,

        @Schema(description = "Fecha en que se realizó la inscripción", example = "11/02/2026")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate fechaInscripcion
) {
}
