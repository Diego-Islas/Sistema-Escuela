package com.diego.escuela.controller;

import com.diego.escuela.docs.ProblemaDoc;
import com.diego.escuela.services.CrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
// Errores generales que pueden ocurrir en los endpoints
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class CrudController<RQ, RS, S extends CrudService<RQ, RS>> {
    protected final S service;

    @GetMapping
    @Operation(summary = "Listar todos los registros", description = "Obtiene todos los registros")
    public ResponseEntity<List<RS>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener registro por ID", description = "Obtiene un registro por su ID")
    // Errores generales que pueden ocurrir en los endpoints
    @ApiResponse(responseCode = "200", description = "Registro encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "404", description = "Registro no encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<RS> obtenerPorId(
            @Parameter(description = "ID del registro", required = true, example = "1")
            @PathVariable @Positive(message = "El ID debe ser un número positivo") Long id
    ) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo registro", description = "Crea un nuevo registro en la base de datos")
    @ApiResponse(responseCode = "201", description = "Registro creado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto al crear el registro (por ejemplo, duplicado)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<RS> registrar(@Valid @RequestBody RQ rq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(rq));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un registro existente", description = "Actualiza un registro existente en la base de datos")
    @ApiResponse(responseCode = "200", description = "Registro actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "404", description = "Registro no encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<RS> actualizar(
            @Parameter(description = "ID del registro", required = true, example = "1")
            @PathVariable @Positive(message = "El ID debe ser un número positivo") Long id,
            @Valid @RequestBody RQ rq
    ) {
        return ResponseEntity.ok(service.actualizar(rq, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro existente", description = "Elimina un registro existente en la base de datos")
    @ApiResponse(responseCode = "204", description = "Registro eliminado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "404", description = "Registro no encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del registro", required = true, example = "1")
            @PathVariable @Positive(message = "El ID debe ser un número positivo") Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
