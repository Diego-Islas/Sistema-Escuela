package com.diego.escuela.enums;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiaSemanaTest {
    @Test
    void obtieneDiaIgnorandoMayusculasYAcentos() {
        assertEquals(DiaSemana.MIERCOLES, DiaSemana.obtenerDiaPorDescripcion("MIÉRCOLES"));
    }

    @Test
    void rechazaDescripcionDesconocida() {
        assertThrows(
                DatoInvalidoException.class,
                () -> DiaSemana.obtenerDiaPorDescripcion("Domingo")
        );
    }
}
