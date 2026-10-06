package com.diego.escuela.exceptions;

// Excepción cuando no se encuentra un recurso solicitado
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}