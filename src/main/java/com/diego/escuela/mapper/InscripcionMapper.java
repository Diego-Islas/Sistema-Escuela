package com.diego.escuela.mapper;

import com.diego.escuela.dto.datos.DatosAlumnoInscripcion;
import com.diego.escuela.dto.datos.DatosGrupoInscripcion;
import com.diego.escuela.dto.inscripciones.InscripcionResponse;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Inscripcion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InscripcionMapper {
    public InscripcionResponse responseAEntidad(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        var alumno = inscripcion.getAlumno();
        Grupo grupo = inscripcion.getGrupo();
        return new InscripcionResponse(
                inscripcion.getId(),
                new DatosAlumnoInscripcion(
                        String.join(" ", alumno.getNombre(),
                                alumno.getApellidoPaterno(),
                                alumno.getApellidoMaterno()),
                        alumno.getMatricula(),
                        alumno.getEmail(),
                        alumno.getFechaIngreso()
                ),
                new DatosGrupoInscripcion(
                        grupo.getCurso().getNombre(),
                        String.join(" ", grupo.getMaestro().getNombre(),
                                grupo.getMaestro().getApellidoPaterno(),
                                grupo.getMaestro().getApellidoMaterno()),
                        grupo.getAula().getNombre(),
                        grupo.getPeriodo()
                ),
                inscripcion.getCalificacion() == null
                        ? null : inscripcion.getCalificacion().getCalificacion(),
                inscripcion.getFechaInscripcion()
        );
    }
}
