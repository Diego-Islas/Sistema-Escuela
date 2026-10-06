package com.diego.escuela.utils;

import com.diego.escuela.exceptions.DatoInvalidoException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class HoraUtils {
    private HoraUtils() {
        /* This utility class should not be instantiated */
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public static void validarHora(String hora, String mensaje) {
        try {
            LocalTime.parse(hora, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static boolean esHoraPosterior(String horaFin, String horaInicio) {
        return LocalTime.parse(horaFin, FORMATTER)
                .isAfter(LocalTime.parse(horaInicio, FORMATTER));
    }
}
