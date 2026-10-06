package com.diego.escuela.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlumnoTest {
    @Test
    void promedioEsCeroConDosDecimalesCuandoNoHayCalificaciones() {
        Alumno alumno = Alumno.builder().build();

        assertEquals(new BigDecimal("0.00"), alumno.calcularPromedio());
    }

    @Test
    void promedioIgnoraInscripcionesSinCalificacion() {
        Inscripcion primera = Inscripcion.builder()
                .calificacion(Calificacion.builder()
                        .calificacion(new BigDecimal("7.5"))
                        .build())
                .build();
        Inscripcion pendiente = Inscripcion.builder().build();
        Inscripcion segunda = Inscripcion.builder()
                .calificacion(Calificacion.builder()
                        .calificacion(new BigDecimal("9.0"))
                        .build())
                .build();
        Alumno alumno = Alumno.builder()
                .inscripciones(List.of(primera, pendiente, segunda))
                .build();

        assertEquals(new BigDecimal("8.25"), alumno.calcularPromedio());
    }
}
