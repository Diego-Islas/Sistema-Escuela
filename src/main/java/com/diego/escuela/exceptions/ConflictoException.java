package com.diego.escuela.exceptions;

// Excepción para representar conflictos de negocio
public class ConflictoException extends RuntimeException {

    public ConflictoException(String message) {
        super(message);
    }
}