package com.diego.escuela.controller;

import com.diego.escuela.dto.datos.DatosCurso;
import com.diego.escuela.dto.maestros.MaestroRequest;
import com.diego.escuela.dto.maestros.MaestroResponse;
import com.diego.escuela.services.maestros.MaestroService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maestros")
@Tag(name = "Maestros", description = "Gestion de maestros de la escuela")
public class MaestroController extends CrudController<MaestroRequest, MaestroResponse, MaestroService> {

    public MaestroController(MaestroService service) {
        super(service);
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<List<DatosCurso>> obtenerCursosDeUnMaestroConId(
            @Parameter(description = "Identificador del maestro", example = "1")
            @PathVariable @Positive(message = "El identificador debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(service.obtenerCursosDeUnMaestroConId(id));
    }
}