package com.diego.escuela.mapper;

import com.diego.escuela.dto.alumnos.AlumnoRequest;
import com.diego.escuela.dto.alumnos.AlumnoResponse;
import com.diego.escuela.dto.datos.DatosCalificacionAlumno;
import com.diego.escuela.entities.Alumno;
import com.diego.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlumnoMapper implements CommonMapper<AlumnoRequest, AlumnoResponse, Alumno> {

    @Override
    public Alumno requestAEntidad(AlumnoRequest request) {
        return request == null ? null : Alumno.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .build();
    }

    public Alumno requestAEntidad(AlumnoRequest request, String matricula, String email) {
        if (request == null) {
            return null;
        }

        Alumno alumno = requestAEntidad(request);
        alumno.asignarDatosAcademicos(matricula, email);
        return alumno;
    }

    @Override
    public AlumnoResponse responseAEntidad(Alumno entidad) {

        return entidad == null ? null : new AlumnoResponse(
                entidad.getId(),
                String.join(" ", entidad.getNombre(), entidad.getApellidoPaterno(), entidad.getApellidoMaterno()),
                entidad.getEmail(),
                entidad.getMatricula(),
                StringCustomUtils.localDateAsString(entidad.getFechaIngreso()),
                entidadADatosCalificacion(entidad),
                entidad.calcularPromedio());
    }

    private List<DatosCalificacionAlumno> entidadADatosCalificacion(Alumno entidad) {
        if (entidad == null || entidad.getInscripciones() == null || entidad.getInscripciones().isEmpty())
            return List.of();

        return entidad.getInscripciones().stream()
                .map(inscripcion -> new DatosCalificacionAlumno(
                        inscripcion.getGrupo().getCurso().getNombre(),
                        inscripcion.getGrupo().getPeriodo(),
                        inscripcion.getCalificacion() != null ? inscripcion.getCalificacion().getCalificacion() : null
                )).toList();
    }
}