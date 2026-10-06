package com.diego.escuela.mapper;

import com.diego.escuela.dto.datos.DatosCurso;
import com.diego.escuela.dto.maestros.MaestroRequest;
import com.diego.escuela.dto.maestros.MaestroResponse;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Maestro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MaestroMapper implements CommonMapper<MaestroRequest, MaestroResponse, Maestro> {

    private final CursoMapper cursoMapper;


    @Override
    public Maestro requestAEntidad(MaestroRequest request) {
        return request == null ? null :
                Maestro.crearMaestro(
                        request.nombre(),
                        request.apellidoPaterno(),
                        request.apellidoMaterno(),
                        request.email(),
                        request.telefono()
                );
    }

    @Override
    public MaestroResponse responseAEntidad(Maestro entidad) {

        return entidad == null ? null :
                new MaestroResponse(
                        entidad.getId(),
                        String.join(" ", entidad.getNombre(),
                                entidad.getApellidoPaterno(),
                                entidad.getApellidoMaterno()),
                        entidad.getEmail(),
                        entidad.getTelefono(),
                        entidadADaDatosCurso(entidad)
                );
    }

    private List<DatosCurso> entidadADaDatosCurso(Maestro entidad) {
        return entidad == null ? List.of() :
                entidad.getGrupos().stream()
                        .map(Grupo::getCurso)
                        .distinct()
                        .map(cursoMapper::entidadADatosCurso)
                        .toList();
    }
}