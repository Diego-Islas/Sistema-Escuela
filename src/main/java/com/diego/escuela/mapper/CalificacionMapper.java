package com.diego.escuela.mapper;

import com.diego.escuela.dto.calificaciones.CalificacionResponse;
import com.diego.escuela.dto.datos.DatosAlumnoInscripcion;
import com.diego.escuela.dto.datos.DatosGrupoInscripcion;
import com.diego.escuela.dto.datos.DatosInscripcionCalificacion;
import com.diego.escuela.entities.Calificacion;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Inscripcion;
import org.springframework.stereotype.Component;

@Component
public class CalificacionMapper {
    public CalificacionResponse responseAEntidad(Calificacion calificacion) {
        if (calificacion == null) {
            return null;
        }

        Inscripcion inscripcion = calificacion.getInscripcion();
        var alumno = inscripcion.getAlumno();
        Grupo grupo = inscripcion.getGrupo();
        DatosAlumnoInscripcion datosAlumno = new DatosAlumnoInscripcion(
                String.join(" ", alumno.getNombre(),
                        alumno.getApellidoPaterno(),
                        alumno.getApellidoMaterno()),
                alumno.getMatricula(),
                alumno.getEmail(),
                alumno.getFechaIngreso()
        );
        DatosGrupoInscripcion datosGrupo = new DatosGrupoInscripcion(
                grupo.getCurso().getNombre(),
                String.join(" ", grupo.getMaestro().getNombre(),
                        grupo.getMaestro().getApellidoPaterno(),
                        grupo.getMaestro().getApellidoMaterno()),
                grupo.getAula().getNombre(),
                grupo.getPeriodo()
        );

        return new CalificacionResponse(
                calificacion.getId(),
                new DatosInscripcionCalificacion(
                        datosAlumno,
                        datosGrupo,
                        inscripcion.getFechaInscripcion()
                ),
                calificacion.getCalificacion(),
                calificacion.getFechaRegistro()
        );
    }
}
