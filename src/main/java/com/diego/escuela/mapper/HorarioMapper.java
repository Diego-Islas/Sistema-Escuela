package com.diego.escuela.mapper;

import com.diego.escuela.dto.datos.DatosGrupoHorario;
import com.diego.escuela.dto.horarios.HorarioResponse;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Horario;
import org.springframework.stereotype.Component;

@Component
public class HorarioMapper {
    public HorarioResponse responseAEntidad(Horario horario) {
        if (horario == null) {
            return null;
        }

        Grupo grupo = horario.getGrupo();
        String nombreMaestro = String.join(
                " ",
                grupo.getMaestro().getNombre(),
                grupo.getMaestro().getApellidoPaterno(),
                grupo.getMaestro().getApellidoMaterno()
        );
        DatosGrupoHorario datosGrupo = new DatosGrupoHorario(
                grupo.getCurso().getNombre(),
                nombreMaestro,
                grupo.getAula().getNombre(),
                grupo.getPeriodo()
        );

        return new HorarioResponse(
                horario.getId(),
                datosGrupo,
                horario.getDia().getDescription() + " "
                        + horario.getHoraInicio() + " "
                        + horario.getHoraFin()
        );
    }
}
