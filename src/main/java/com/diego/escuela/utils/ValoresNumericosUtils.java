package com.diego.escuela.utils;

import com.diego.escuela.exceptions.DatoInvalidoException;

import java.math.BigDecimal;

public class ValoresNumericosUtils {
    private ValoresNumericosUtils() {
        /* This utility class should not be instantiated */
    }

    private static final String NUMEROS = "0123456789";

    public static <N extends Number> void validarNumeroRequerido(N numero, String mensaje) {
        if (numero == null)
            throw new DatoInvalidoException(mensaje);
    }

    public static void validarEnteroPositvo(Integer numero, String mensaje) {
        validarNumeroRequerido(numero, mensaje);
        if (numero <= 0)
            throw new DatoInvalidoException(mensaje);
    }

    public static void validarBigDecimalPositivo(BigDecimal numero, String mensaje) {
        validarNumeroRequerido(numero, mensaje);
        if (numero.compareTo(BigDecimal.ZERO) <= 0)
            throw new DatoInvalidoException(mensaje);
    }

    public static void validarFloatPositivo(Float numero, String mensaje) {
        validarNumeroRequerido(numero, mensaje);
        if (numero <= 0)
            throw new DatoInvalidoException(mensaje);
    }

    public static void validarCharSeaNumero(char c, String mensaje) {
        if (!NUMEROS.contains(String.valueOf(c)))
            throw new DatoInvalidoException(mensaje);
    }

    public static void validarStringSoloNumeros(String texto, String mensaje) {
        for (int i = 0; i < texto.length(); i++) {
            validarCharSeaNumero(texto.charAt(i), mensaje);
        }
    }

}