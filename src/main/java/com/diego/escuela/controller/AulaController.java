package com.diego.escuela.controller;

import com.diego.escuela.dto.aulas.AulaRequest;
import com.diego.escuela.dto.aulas.AulaResponse;
import com.diego.escuela.services.aulas.AulaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aulas")
@Tag(name = "Aulas", description = "Gestión de aulas de la escuela")
public class AulaController extends CrudController<AulaRequest, AulaResponse, AulaService> {
    public AulaController(AulaService service) {
        super(service);
    }
}
