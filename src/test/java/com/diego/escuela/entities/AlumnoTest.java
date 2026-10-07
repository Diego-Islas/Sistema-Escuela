package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlumnoTest {
    @Test
    void crearNormalizaDatosPersonalesYDetectaCambios() {
        Alumno alumno = Alumno.crear(" Carlos ", " Pérez ", " López ");
        alumno.asignarDatosAcademicos("PELOCA2601", " CARLOS@ESCUELA.COM ");

        assertEquals("Carlos", alumno.getNombre());
        assertEquals("Pérez", alumno.getApellidoPaterno());
        assertEquals("López", alumno.getApellidoMaterno());
        assertEquals("PELOCA2601", alumno.getMatricula());
        assertEquals("carlos@escuela.com", alumno.getEmail());
        assertFalse(alumno.cambioEnDatosPersonales("Carlos", "Pérez", "López"));
        assertTrue(alumno.cambioEnDatosPersonales("Carlos", "Gómez", "López"));
    }

    @Test
    void actualizarCambiaDatosPersonalesYAcademicos() {
        Alumno alumno = Alumno.crear("Carlos", "Pérez", "López");
        alumno.asignarDatosAcademicos("PELOCA2601", "carlos@escuela.com");

        alumno.actualizar(" Ana ", " Gómez ", " Ruiz ", "GORUAN2602", " ANA@ESCUELA.COM ");

        assertEquals("Ana", alumno.getNombre());
        assertEquals("Gómez", alumno.getApellidoPaterno());
        assertEquals("Ruiz", alumno.getApellidoMaterno());
        assertEquals("GORUAN2602", alumno.getMatricula());
        assertEquals("ana@escuela.com", alumno.getEmail());
    }

    @Test
    void rechazaDatosPersonalesYAcademicosInvalidos() {
        assertThrows(DatoInvalidoException.class, () -> Alumno.crear("", "Pérez", "López"));
        Alumno alumno = Alumno.builder().build();

        assertThrows(DatoInvalidoException.class,
                () -> alumno.asignarDatosAcademicos("", "ana@escuela.com"));
        assertThrows(DatoInvalidoException.class,
                () -> alumno.asignarDatosAcademicos("MATRICULA123", "ana@escuela.com"));
        assertThrows(DatoInvalidoException.class,
                () -> alumno.asignarDatosAcademicos("PELOCA2601", ""));
    }

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
