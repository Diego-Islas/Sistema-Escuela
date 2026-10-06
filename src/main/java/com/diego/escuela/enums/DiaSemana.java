package com.diego.escuela.enums;

import com.diego.escuela.exceptions.DatoInvalidoException;
import com.diego.escuela.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiaSemana {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado");
    private final String description;

    public static DiaSemana obtenerDiaPorDescripcion(String description) {
        StringCustomUtils.validarNoVacio(description, "El horario es requerido");
        String descripcionNormalizada = StringCustomUtils.normalizarTexto(description);

        for (DiaSemana diaSemana : values()) {
            if (
                    StringCustomUtils
                            .normalizarTexto(
                                    diaSemana.getDescription()
                            )
                            .equals(descripcionNormalizada)
            )
                return diaSemana;
        }

        throw new DatoInvalidoException("No existe un dia con descripcion: " + description);
    }
}