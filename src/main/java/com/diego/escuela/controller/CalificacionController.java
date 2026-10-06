package com.diego.escuela.controller;

import com.diego.escuela.dto.calificaciones.CalificacionRequest;
import com.diego.escuela.dto.calificaciones.CalificacionResponse;
import com.diego.escuela.services.calificaciones.CalificacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
@Tag(name = "API Calificaciones", description = "Gestión de calificaciones académicas")
public class CalificacionController
        extends CrudController<CalificacionRequest, CalificacionResponse, CalificacionService> {
    public CalificacionController(CalificacionService service) {
        super(service);
    }
}
