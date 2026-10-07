package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AulaTest {
    @Test
    void crearRecortaNombre() {
        Aula aula = Aula.crear(" Aula 101 ", 30);

        assertEquals("Aula 101", aula.getNombre());
        assertEquals(30, aula.getCapacidad());
    }

    @Test
    void rechazaCapacidadNulaOCero() {
        assertThrows(DatoInvalidoException.class, () -> Aula.crear("Aula 101", null));
        assertThrows(DatoInvalidoException.class, () -> Aula.crear("Aula 101", 0));
    }

    @Test
    void actualizaNombreYCapacidad() {
        Aula aula = Aula.crear("Aula 101", 30);

        aula.actualizar("Aula 202", 25);

        assertEquals("Aula 202", aula.getNombre());
        assertEquals(25, aula.getCapacidad());
    }

    @Test
    void rechazaNombreDemasiadoCorto() {
        assertThrows(DatoInvalidoException.class, () -> Aula.crear("A1", 30));
    }
}
