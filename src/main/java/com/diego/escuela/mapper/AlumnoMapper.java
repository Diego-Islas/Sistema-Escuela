package com.diego.escuela.mapper;

import com.diego.escuela.dto.alumnos.AlumnoRequest;
import com.diego.escuela.dto.alumnos.AlumnoResponse;
import com.diego.escuela.dto.datos.DatosCalificacionAlumno;
import com.diego.escuela.entities.Alumno;
import com.diego.escuela.entities.Calificacion;
import com.diego.escuela.entities.Inscripcion;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class AlumnoMapper {
    public Alumno crearEntidad(AlumnoRequest request, String matricula, String email) {
        return Alumno.crear(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                matricula,
                email
        );
    }

    public AlumnoResponse responseAEntidad(Alumno alumno) {
        List<DatosCalificacionAlumno> calificaciones = alumno.getInscripciones().stream()
                .map(this::inscripcionADatosCalificacion)
                .toList();
        List<BigDecimal> notas = alumno.getInscripciones().stream()
                .map(Inscripcion::getCalificacion)
                .map(this::obtenerNota)
                .filter(nota -> nota != null)
                .toList();

        BigDecimal promedio = notas.isEmpty()
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : notas.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(notas.size()), 2, RoundingMode.HALF_UP);

        return new AlumnoResponse(
                alumno.getId(),
                String.join(" ", alumno.getNombre(),
                        alumno.getApellidoPaterno(),
                        alumno.getApellidoMaterno()),
                alumno.getEmail(),
                alumno.getMatricula(),
                alumno.getFechaIngreso(),
                calificaciones,
                promedio
        );
    }

    private DatosCalificacionAlumno inscripcionADatosCalificacion(Inscripcion inscripcion) {
        return new DatosCalificacionAlumno(
                inscripcion.getGrupo().getCurso().getNombre(),
                inscripcion.getGrupo().getPeriodo(),
                obtenerNota(inscripcion.getCalificacion())
        );
    }

    private BigDecimal obtenerNota(Calificacion calificacion) {
        return calificacion == null ? null : calificacion.getCalificacion();
    }
}
