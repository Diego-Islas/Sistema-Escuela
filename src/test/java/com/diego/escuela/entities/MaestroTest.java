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
        assertEquals("Ana Pérez López", maestro.getNombreCompleto());
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

    @Test
    void actualizarNormalizaDatos() {
        Maestro maestro = Maestro.crearMaestro(
                "Ana", "Pérez", "López", "ana@escuela.com", "5512345678"
        );

        maestro.actualizarMaestro(
                " Luis ", " Gómez ", " Ruiz ", " LUIS@ESCUELA.COM ", "5598765432"
        );

        assertEquals("Luis", maestro.getNombre());
        assertEquals("Gómez", maestro.getApellidoPaterno());
        assertEquals("Ruiz", maestro.getApellidoMaterno());
        assertEquals("luis@escuela.com", maestro.getEmail());
        assertEquals("5598765432", maestro.getTelefono());
    }

    @Test
    void rechazaEmailVacio() {
        assertThrows(
                DatoInvalidoException.class,
                () -> Maestro.crearMaestro("Ana", "Pérez", "López", " ", "5512345678")
        );
    }
}
