package com.diego.escuela.mapper;

import com.diego.escuela.dto.datos.DatosAula;
import com.diego.escuela.dto.datos.DatosMaestro;
import com.diego.escuela.dto.grupos.GrupoRequest;
import com.diego.escuela.dto.grupos.GrupoResponse;
import com.diego.escuela.entities.Aula;
import com.diego.escuela.entities.Curso;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Maestro;
import org.springframework.stereotype.Component;

@Component
public class GrupoMapper {
    private final CursoMapper cursoMapper;
    private final HorarioMapper horarioMapper;

    public GrupoMapper(CursoMapper cursoMapper, HorarioMapper horarioMapper) {
        this.cursoMapper = cursoMapper;
        this.horarioMapper = horarioMapper;
    }

    public Grupo requestAEntidad(GrupoRequest request, Curso curso, Maestro maestro, Aula aula) {
        return request == null ? null : Grupo.crear(curso, maestro, aula, request.periodo());
    }

    public GrupoResponse responseAEntidad(Grupo grupo) {
        if (grupo == null) {
            return null;
        }

        return new GrupoResponse(
                grupo.getId(),
                cursoMapper.entidadADatosCurso(grupo.getCurso()),
                new DatosMaestro(
                        grupo.getMaestro().getNombreCompleto(),
                        grupo.getMaestro().getEmail(),
                        grupo.getMaestro().getTelefono()
                ),
                new DatosAula(grupo.getAula().getNombre(), grupo.getAula().getCapacidad()),
                grupo.getHorarios().stream()
                        .map(horario -> horarioMapper.formatearHorario(horario, " - "))
                        .toList(),
                grupo.getPeriodo()
        );
    }
}
