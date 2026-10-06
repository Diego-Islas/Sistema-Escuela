package com.diego.escuela.dto.grupos;

import com.diego.escuela.dto.datos.DatosAula;
import com.diego.escuela.dto.datos.DatosCurso;
import com.diego.escuela.dto.datos.DatosMaestro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Datos de respuesta de un grupo")
public record GrupoResponse(
        @Schema(description = "Identificador del grupo", example = "1")
        Long id,

        @Schema(description = "Datos del curso impartido en el grupo")
        DatosCurso curso,

        @Schema(description = "Datos del maestro asignado al grupo")
        DatosMaestro maestro,

        @Schema(description = "Datos del aula asignada al grupo")
        DatosAula aula,

        @Schema(description = "Horarios asignados al grupo", example = "[\"Lunes 08:00 - 10:00\", \"Miércoles 08:00 - 10:00\"]")
        List<String> horarios,

        @Schema(description = "Periodo académico del grupo", example = "2026-01")
        String periodo
) {
}
