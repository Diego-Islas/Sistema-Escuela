package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GrupoTest {
    @Test
    void permiteLosDosFormatosDePeriodoDelContrato() {
        Curso curso = Curso.crear("Matemáticas I", "Fundamentos", 6);
        Maestro maestro = Maestro.crearMaestro(
                "Ana", "Pérez", "López", "ana@escuela.com", "5512345678"
        );
        Aula aula = Aula.crear("Aula 101", 30);

        assertEquals("2026-01", Grupo.crear(curso, maestro, aula, "2026-01").getPeriodo());
        assertEquals("2025-1", Grupo.crear(curso, maestro, aula, "2025-1").getPeriodo());
    }

    @Test
    void rechazaPeriodoFueraDeLosFormatosDelContrato() {
        Curso curso = Curso.crear("Matemáticas I", "Fundamentos", 6);
        Maestro maestro = Maestro.crearMaestro(
                "Ana", "Pérez", "López", "ana@escuela.com", "5512345678"
        );
        Aula aula = Aula.crear("Aula 101", 30);

        assertThrows(
                DatoInvalidoException.class,
                () -> Grupo.crear(curso, maestro, aula, "2025-13")
        );
    }

    @Test
    void requiereCursoMaestroYAula() {
        Curso curso = Curso.crear("Matemáticas I", "Fundamentos", 6);
        Maestro maestro = Maestro.crearMaestro(
                "Ana", "Pérez", "López", "ana@escuela.com", "5512345678"
        );
        Aula aula = Aula.crear("Aula 101", 30);

        assertThrows(DatoInvalidoException.class, () -> Grupo.crear(null, maestro, aula, "2026-01"));
        assertThrows(DatoInvalidoException.class, () -> Grupo.crear(curso, null, aula, "2026-01"));
        assertThrows(DatoInvalidoException.class, () -> Grupo.crear(curso, maestro, null, "2026-01"));
    }
}
