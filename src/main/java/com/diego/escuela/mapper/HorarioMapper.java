package com.diego.escuela.mapper;

import com.diego.escuela.dto.datos.DatosGrupoHorario;
import com.diego.escuela.dto.horarios.HorarioResponse;
import com.diego.escuela.dto.horarios.HorarioRequest;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Horario;
import com.diego.escuela.enums.DiaSemana;
import org.springframework.stereotype.Component;

@Component
public class HorarioMapper {
    public Horario requestAEntidad(HorarioRequest request, Grupo grupo, DiaSemana dia) {
        return request == null ? null
                : Horario.crear(grupo, dia, request.horaInicio(), request.horaFin());
    }

    public HorarioResponse responseAEntidad(Horario horario) {
        if (horario == null) {
            return null;
        }

        Grupo grupo = horario.getGrupo();
        DatosGrupoHorario datosGrupo = new DatosGrupoHorario(
                grupo.getCurso().getNombre(),
                grupo.getMaestro().getNombreCompleto(),
                grupo.getAula().getNombre(),
                grupo.getPeriodo()
        );

        return new HorarioResponse(
                horario.getId(),
                datosGrupo,
                formatearHorario(horario, " - ")
        );
    }

    public String formatearHorario(Horario horario, String separadorEntreHoras) {
        return horario.getDia().getDescription() + " "
                + horario.getHoraInicio() + separadorEntreHoras + horario.getHoraFin();
    }

}
