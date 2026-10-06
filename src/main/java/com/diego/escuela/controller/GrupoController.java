package com.diego.escuela.controller;

import com.diego.escuela.dto.grupos.GrupoRequest;
import com.diego.escuela.dto.grupos.GrupoResponse;
import com.diego.escuela.services.grupos.GrupoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grupos")
@Tag(name = "Grupos", description = "Gestión de grupos de la escuela")
public class GrupoController extends CrudController<GrupoRequest, GrupoResponse, GrupoService> {
    public GrupoController(GrupoService service) {
        super(service);
    }
}
