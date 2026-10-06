package com.diego.escuela.dto.alumnos;

import com.diego.escuela.dto.datos.DatosCalificacionAlumno;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Datos de respuesta de un alumno y su historial académico")
public record AlumnoResponse(
        @Schema(description = "Identificador del alumno", example = "1")
        Long id,

        @Schema(description = "Nombre completo del alumno", example = "Carlos González Ramírez")
        String nombre,

        @Schema(description = "Correo institucional generado",
                example = "atr.26.carlos.gonzalez.ramirez.goracara2601@escuela.com.mx")
        String email,

        @Schema(description = "Matrícula generada", example = "GORACA2601")
        String matricula,

        @Schema(description = "Fecha de ingreso con formato dd/MM/yyyy", example = "10/01/2025")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate fechaIngreso,

        @Schema(description = "Cursos inscritos con su calificación, null si está pendiente")
        List<DatosCalificacionAlumno> calificaciones,

        @Schema(description = "Promedio de calificaciones no nulas; 0.00 si aún no hay calificaciones",
                example = "8.00")
        BigDecimal promedio
) {
}
