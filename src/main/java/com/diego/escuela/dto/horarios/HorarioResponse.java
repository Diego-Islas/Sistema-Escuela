package com.diego.escuela.dto.horarios;

import com.diego.escuela.dto.datos.DatosGrupoHorario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta de un horario")
public record HorarioResponse(
        @Schema(description = "Identificador del horario", example = "1")
        Long id,

        @Schema(description = "Datos del grupo al que pertenece el horario")
        DatosGrupoHorario grupo,

        @Schema(description = "Día y horas del horario", example = "Lunes 08:00 10:00")
        String horario
) {
}
