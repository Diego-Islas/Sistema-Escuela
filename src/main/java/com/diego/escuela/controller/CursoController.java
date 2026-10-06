package com.diego.escuela.controller;

import com.diego.escuela.dto.cursos.CursoRequest;
import com.diego.escuela.dto.cursos.CursoResponse;
import com.diego.escuela.services.cursos.CursoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "API Cursos", description = "Gestión de cursos de la escuela")
public class CursoController extends CrudController<CursoRequest, CursoResponse, CursoService> {
    public CursoController(CursoService service) {
        super(service);
    }
}
