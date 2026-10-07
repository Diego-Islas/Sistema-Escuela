package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CursoTest {
    @Test
    void crearRecortaTextoYConservaDatos() {
        Curso curso = Curso.crear("  Matemáticas I ", " Fundamentos ", 6);

        assertEquals("Matemáticas I", curso.getNombre());
        assertEquals("Fundamentos", curso.getDescripcion());
        assertEquals(6, curso.getCreditos());
    }

    @Test
    void rechazaCursoConCreditosNoPositivos() {
        assertThrows(
                DatoInvalidoException.class,
                () -> Curso.crear("Matemáticas I", "Fundamentos", 0)
        );
    }

    @Test
    void actualizaSinReemplazarLaEntidad() {
        Curso curso = Curso.crear("Matemáticas I", "Fundamentos", 6);

        curso.actualizar("Bases de Datos", "Introducción", 5);

        assertEquals("Bases de Datos", curso.getNombre());
        assertEquals("Introducción", curso.getDescripcion());
        assertEquals(5, curso.getCreditos());
    }
}
