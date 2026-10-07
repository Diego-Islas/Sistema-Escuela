package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalificacionTest {
    @Test
    void crearAsociaNotaYFechaAInscripcion() {
        Inscripcion inscripcion = Inscripcion.builder().build();

        Calificacion calificacion = Calificacion.crear(inscripcion, new BigDecimal("8.5"));

        assertEquals(inscripcion, calificacion.getInscripcion());
        assertEquals(calificacion, inscripcion.getCalificacion());
        assertEquals(new BigDecimal("8.5"), calificacion.getCalificacion());
        assertNotNull(calificacion.getFechaRegistro());
    }

    @Test
    void rechazaCalificacionFueraDelRangoOCualquieraNula() {
        Inscripcion inscripcion = Inscripcion.builder().build();

        assertThrows(
                DatoInvalidoException.class,
                () -> Calificacion.crear(inscripcion, new BigDecimal("-0.1"))
        );
        assertThrows(
                DatoInvalidoException.class,
                () -> Calificacion.crear(inscripcion, new BigDecimal("10.1"))
        );
        assertThrows(
                DatoInvalidoException.class,
                () -> Calificacion.crear(inscripcion, null)
        );
    }
}
