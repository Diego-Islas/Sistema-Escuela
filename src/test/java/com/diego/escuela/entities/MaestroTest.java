package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaestroTest {
    @Test
    void crearNormalizaTextoDeEmailYRecortaCampos() {
        Maestro maestro = Maestro.crearMaestro(
                " Ana ",
                " Pérez ",
                " López ",
                " ANA@ESCUELA.COM ",
                "5512345678"
        );

        assertEquals("Ana", maestro.getNombre());
        assertEquals("ana@escuela.com", maestro.getEmail());
        assertEquals("5512345678", maestro.getTelefono());
    }

    @Test
    void rechazaTelefonoQueNoTengaDiezDigitos() {
        assertThrows(
                DatoInvalidoException.class,
                () -> Maestro.crearMaestro("Ana", "Pérez", "López", "ana@escuela.com", "123")
        );
    }

    @Test
    void rechazaTelefonoConCaracteresNoNumericos() {
        assertThrows(
                DatoInvalidoException.class,
                () -> Maestro.crearMaestro("Ana", "Pérez", "López", "ana@escuela.com", "55123A5678")
        );
    }
}
