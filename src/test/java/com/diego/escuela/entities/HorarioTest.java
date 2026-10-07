package com.diego.escuela.entities;

import com.diego.escuela.enums.DiaSemana;
import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HorarioTest {
    @Test
    void crearAceptaRangoValido() {
        Horario horario = Horario.crear(
                Grupo.builder().build(),
                DiaSemana.LUNES,
                "08:00",
                "10:00"
        );

        assertEquals("08:00", horario.getHoraInicio());
        assertEquals("10:00", horario.getHoraFin());
    }

    @Test
    void rechazaHoraFinAnteriorOIgualAInicio() {
        Grupo grupo = Grupo.builder().build();

        assertThrows(
                DatoInvalidoException.class,
                () -> Horario.crear(grupo, DiaSemana.LUNES, "10:00", "08:00")
        );
        assertThrows(
                DatoInvalidoException.class,
                () -> Horario.crear(grupo, DiaSemana.LUNES, "08:00", "08:00")
        );
    }

    @Test
    void rechazaHoraConFormatoInvalido() {
        assertThrows(
                DatoInvalidoException.class,
                () -> Horario.crear(Grupo.builder().build(), DiaSemana.LUNES, "8:00", "10:00")
        );
    }
}
