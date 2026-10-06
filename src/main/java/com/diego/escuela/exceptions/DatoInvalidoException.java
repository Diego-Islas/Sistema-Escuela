package com.diego.escuela.exceptions;

// Excepción para datos que no cumplen las reglas esperadas
public class DatoInvalidoException extends RuntimeException {

    public DatoInvalidoException(String message) {
        super(message);
    }
}