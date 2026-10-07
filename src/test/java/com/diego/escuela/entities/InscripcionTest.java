package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InscripcionTest {
    @Test
    void crearAsociaAlumnoGrupoYFechaActual() {
        Alumno alumno = Alumno.builder().build();
        Grupo grupo = Grupo.builder().build();

        Inscripcion inscripcion = Inscripcion.crear(alumno, grupo);

        assertSame(alumno, inscripcion.getAlumno());
        assertSame(grupo, inscripcion.getGrupo());
        assertEquals(LocalDate.now(), inscripcion.getFechaInscripcion());
    }

    @Test
    void crearRechazaAlumnoOGrupoNulos() {
        Grupo grupo = Grupo.builder().build();
        Alumno alumno = Alumno.builder().build();

        assertThrows(DatoInvalidoException.class, () -> Inscripcion.crear(null, grupo));
        assertThrows(DatoInvalidoException.class, () -> Inscripcion.crear(alumno, null));
    }

    @Test
    void actualizarCambiaAlumnoYGrupoYValidaRelaciones() {
        Alumno alumnoInicial = Alumno.builder().build();
        Grupo grupoInicial = Grupo.builder().build();
        Alumno alumnoNuevo = Alumno.builder().build();
        Grupo grupoNuevo = Grupo.builder().build();
        Inscripcion inscripcion = Inscripcion.crear(alumnoInicial, grupoInicial);

        inscripcion.actualizar(alumnoNuevo, grupoNuevo);

        assertSame(alumnoNuevo, inscripcion.getAlumno());
        assertSame(grupoNuevo, inscripcion.getGrupo());
        assertThrows(DatoInvalidoException.class, () -> inscripcion.actualizar(null, grupoNuevo));
        assertThrows(DatoInvalidoException.class, () -> inscripcion.actualizar(alumnoNuevo, null));
    }

    @Test
    void asignarYQuitarCalificacionMantieneLaRelacionBidireccional() {
        Inscripcion inscripcion = Inscripcion.builder().build();
        Calificacion calificacion = Calificacion.builder().build();

        inscripcion.asignarCalificacion(calificacion);
        assertSame(calificacion, inscripcion.getCalificacion());

        inscripcion.quitarCalificacion(Calificacion.builder().build());
        assertSame(calificacion, inscripcion.getCalificacion());

        inscripcion.quitarCalificacion(calificacion);
        assertNull(inscripcion.getCalificacion());
        assertThrows(DatoInvalidoException.class, () -> inscripcion.asignarCalificacion(null));
    }
}
