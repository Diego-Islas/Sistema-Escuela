package com.diego.escuela.dto.horarios;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos necesarios para registrar o actualizar un horario")
public record HorarioRequest(
        @Schema(description = "Identificador del grupo", example = "1")
        @NotNull(message = "El identificador del grupo es requerido")
        @Positive(message = "El identificador del grupo debe ser positivo")
        Long idGrupo,

        @Schema(description = "Día de la semana", example = "Lunes")
        @NotBlank(message = "El día es requerido")
        String dia,

        @Schema(description = "Hora de inicio en formato HH:mm", example = "08:00")
        @NotBlank(message = "La hora de inicio es requerida")
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de inicio debe tener formato HH:mm")
        String horaInicio,

        @Schema(description = "Hora de fin en formato HH:mm", example = "10:00")
        @NotBlank(message = "La hora de fin es requerida")
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de fin debe tener formato HH:mm")
        String horaFin
) {
}
