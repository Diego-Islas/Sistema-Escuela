package com.diego.escuela.services.calificaciones;

import com.diego.escuela.dto.calificaciones.CalificacionRequest;
import com.diego.escuela.dto.calificaciones.CalificacionResponse;
import com.diego.escuela.entities.Calificacion;
import com.diego.escuela.entities.Inscripcion;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.mapper.CalificacionMapper;
import com.diego.escuela.repositories.CalificacionRepository;
import com.diego.escuela.repositories.InscripcionRepository;
import com.diego.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CalificacionServiceImpl implements CalificacionService {
    private final CalificacionRepository calificacionRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CalificacionMapper calificacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponse> listar() {
        return calificacionRepository.findAll().stream()
                .map(calificacionMapper::responseAEntidad)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CalificacionResponse obtenerPorId(Long id) {
        return calificacionMapper.responseAEntidad(obtenerCalificacion(id));
    }

    @Override
    public CalificacionResponse registrar(CalificacionRequest request) {
        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());
        if (calificacionRepository.existsByInscripcionId(inscripcion.getId())) {
            throw new ConflictoException("Ya existe una calificación para esa inscripción.");
        }

        Calificacion guardada = calificacionRepository.saveAndFlush(
                Calificacion.crear(inscripcion, request.calificacion())
        );
        log.info("Calificación registrada con id {}", guardada.getId());
        return calificacionMapper.responseAEntidad(guardada);
    }

    @Override
    public CalificacionResponse actualizar(CalificacionRequest request, Long id) {
        Calificacion calificacion = obtenerCalificacion(id);
        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());
        if (calificacionRepository.existsByInscripcionIdAndIdNot(inscripcion.getId(), id)) {
            throw new ConflictoException("Ya existe una calificación para esa inscripción.");
        }

        calificacion.actualizar(inscripcion, request.calificacion());
        Calificacion actualizada = calificacionRepository.saveAndFlush(calificacion);
        log.info("Calificación actualizada con id {}", actualizada.getId());
        return calificacionMapper.responseAEntidad(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Calificacion calificacion = obtenerCalificacion(id);
        calificacion.getInscripcion().quitarCalificacion(calificacion);
        calificacionRepository.delete(calificacion);
        calificacionRepository.flush();
        log.info("Calificación eliminada con id {}", id);
    }

    private Calificacion obtenerCalificacion(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                calificacionRepository, id, Calificacion.class
        );
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                inscripcionRepository, id, Inscripcion.class
        );
    }
}
