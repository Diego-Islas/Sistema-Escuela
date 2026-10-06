package com.diego.escuela.mapper;

import com.diego.escuela.dto.datos.DatosAula;
import com.diego.escuela.dto.datos.DatosMaestro;
import com.diego.escuela.dto.grupos.GrupoResponse;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Horario;
import org.springframework.stereotype.Component;

@Component
public class GrupoMapper {
    private final CursoMapper cursoMapper;

    public GrupoMapper(CursoMapper cursoMapper) {
        this.cursoMapper = cursoMapper;
    }

    public GrupoResponse responseAEntidad(Grupo grupo) {
        if (grupo == null) {
            return null;
        }

        return new GrupoResponse(
                grupo.getId(),
                cursoMapper.entidadADatosCurso(grupo.getCurso()),
                new DatosMaestro(
                        String.join(" ", grupo.getMaestro().getNombre(),
                                grupo.getMaestro().getApellidoPaterno(),
                                grupo.getMaestro().getApellidoMaterno()),
                        grupo.getMaestro().getEmail(),
                        grupo.getMaestro().getTelefono()
                ),
                new DatosAula(grupo.getAula().getNombre(), grupo.getAula().getCapacidad()),
                grupo.getHorarios().stream()
                        .map(this::formatearHorario)
                        .toList(),
                grupo.getPeriodo()
        );
    }

    private String formatearHorario(Horario horario) {
        return horario.getDia().getDescription() + " "
                + horario.getHoraInicio() + " - " + horario.getHoraFin();
    }
}
