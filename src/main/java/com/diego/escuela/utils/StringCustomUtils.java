package com.diego.escuela.utils;

import com.diego.escuela.exceptions.DatoInvalidoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringCustomUtils {
    private StringCustomUtils() {
        /* This utility class should not be instantiated */
    }

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void validarNoVacio(String texto, String mensaje) {

        if (texto == null || texto.trim().isBlank())
            throw new DatoInvalidoException(mensaje);
    }

    public static void validarTamanio(
            String texto,
            Integer min,
            Integer max,
            String mensaje) {

        validarNoVacio(texto, mensaje);

        if (texto.length() < min || texto.length() > max)
            throw new DatoInvalidoException(mensaje);
    }

    public static String normalizarTexto(String texto) {

        return texto.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ü", "u")
                .replace("ñ", "n");
    }

    public static String localDateAsString(LocalDate fecha) {
        return fecha == null ? null : fecha.format(FORMATO_FECHA);
    }
}