package com.diego.escuela.controller;

import com.diego.escuela.dto.inscripciones.InscripcionRequest;
import com.diego.escuela.dto.inscripciones.InscripcionResponse;
import com.diego.escuela.services.inscripciones.InscripcionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "API Inscripciones", description = "Gestión de inscripciones de alumnos")
public class InscripcionController
        extends CrudController<InscripcionRequest, InscripcionResponse, InscripcionService> {
    public InscripcionController(InscripcionService service) {
        super(service);
    }
}
