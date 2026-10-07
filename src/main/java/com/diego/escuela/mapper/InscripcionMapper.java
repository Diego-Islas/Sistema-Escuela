package com.diego.escuela.mapper;

import com.diego.escuela.dto.datos.DatosAlumnoInscripcion;
import com.diego.escuela.dto.datos.DatosGrupoInscripcion;
import com.diego.escuela.dto.inscripciones.InscripcionRequest;
import com.diego.escuela.dto.inscripciones.InscripcionResponse;
import com.diego.escuela.entities.Alumno;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Inscripcion;
import org.springframework.stereotype.Component;

@Component
public class InscripcionMapper {
    public Inscripcion requestAEntidad(InscripcionRequest request, Alumno alumno, Grupo grupo) {
        return request == null ? null : Inscripcion.crear(alumno, grupo);
    }

    public InscripcionResponse responseAEntidad(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        Alumno alumno = inscripcion.getAlumno();

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
                        grupo.getMaestro().getNombreCompleto(),
                        grupo.getAula().getNombre(),
                        grupo.getPeriodo()
                ),
                inscripcion.getCalificacion() == null
                        ? null : inscripcion.getCalificacion().getCalificacion(),
                inscripcion.getFechaInscripcion()
        );
    }
}
