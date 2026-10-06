package com.diego.escuela.controller;

import com.diego.escuela.dto.horarios.HorarioRequest;
import com.diego.escuela.dto.horarios.HorarioResponse;
import com.diego.escuela.services.horarios.HorarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horarios")
@Tag(name = "Horarios", description = "Gestión de horarios de la escuela")
public class HorarioController extends CrudController<HorarioRequest, HorarioResponse, HorarioService> {
    public HorarioController(HorarioService service) {
        super(service);
    }
}
