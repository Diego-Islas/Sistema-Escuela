package com.diego.escuela.mapper;

import com.diego.escuela.dto.cursos.CursoRequest;
import com.diego.escuela.dto.cursos.CursoResponse;
import com.diego.escuela.dto.datos.DatosCurso;
import com.diego.escuela.entities.Curso;

import org.springframework.stereotype.Component;

@Component
public class CursoMapper implements CommonMapper<CursoRequest, CursoResponse, Curso> {
    @Override
    public Curso requestAEntidad(CursoRequest request) {
        return request == null
                ? null :
                Curso.crear(
                        request.nombre(),
                        request.descripcion(),
                        request.creditos()
                );
    }

    @Override
    public CursoResponse responseAEntidad(Curso entidad) {
        return entidad == null
                ? null :
                new CursoResponse(
                        entidad.getId(),
                        entidad.getNombre(),
                        entidad.getDescripcion(),
                        entidad.getCreditos()
                );
    }

    public DatosCurso entidadADatosCurso(Curso curso) {
        return curso == null
                ? null :
                new DatosCurso(
                        curso.getNombre(),
                        curso.getDescripcion(),
                        curso.getCreditos()
                );
    }
}
