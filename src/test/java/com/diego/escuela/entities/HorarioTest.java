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

    @Test
    void rechazaGrupoDiaYOHorasRequeridas() {
        Grupo grupo = Grupo.builder().build();

        assertThrows(DatoInvalidoException.class,
                () -> Horario.crear(null, DiaSemana.LUNES, "08:00", "10:00"));
        assertThrows(DatoInvalidoException.class,
                () -> Horario.crear(grupo, null, "08:00", "10:00"));
        assertThrows(DatoInvalidoException.class,
                () -> Horario.crear(grupo, DiaSemana.LUNES, " ", "10:00"));
    }

    @Test
    void actualizarReemplazaGrupoDiaYHoras() {
        Grupo grupoOriginal = Grupo.builder().build();
        Grupo grupoNuevo = Grupo.builder().build();
        Horario horario = Horario.crear(grupoOriginal, DiaSemana.LUNES, "08:00", "10:00");

        horario.actualizar(grupoNuevo, DiaSemana.MARTES, "10:00", "12:00");

        assertEquals(grupoNuevo, horario.getGrupo());
        assertEquals(DiaSemana.MARTES, horario.getDia());
        assertEquals("10:00", horario.getHoraInicio());
        assertEquals("12:00", horario.getHoraFin());
    }
}
