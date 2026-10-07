package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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

    @Test
    void actualizaNotaYCambiaLaInscripcionAsociada() {
        Inscripcion anterior = Inscripcion.builder().build();
        Inscripcion nueva = Inscripcion.builder().build();
        Calificacion calificacion = Calificacion.crear(anterior, new BigDecimal("8.5"));

        calificacion.actualizar(nueva, new BigDecimal("9.0"));

        assertNull(anterior.getCalificacion());
        assertSame(calificacion, nueva.getCalificacion());
        assertSame(nueva, calificacion.getInscripcion());
        assertEquals(new BigDecimal("9.0"), calificacion.getCalificacion());
    }

    @Test
    void permiteLimitesDeCalificacion() {
        Inscripcion cero = Inscripcion.builder().build();
        Inscripcion diez = Inscripcion.builder().build();

        assertEquals(BigDecimal.ZERO, Calificacion.crear(cero, BigDecimal.ZERO).getCalificacion());
        assertEquals(BigDecimal.TEN, Calificacion.crear(diez, BigDecimal.TEN).getCalificacion());
    }
}
