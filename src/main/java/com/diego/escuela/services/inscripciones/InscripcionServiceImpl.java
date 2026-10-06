package com.diego.escuela.services.inscripciones;

import com.diego.escuela.dto.inscripciones.InscripcionRequest;
import com.diego.escuela.dto.inscripciones.InscripcionResponse;
import com.diego.escuela.entities.Alumno;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Inscripcion;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.InscripcionMapper;
import com.diego.escuela.repositories.AlumnoRepository;
import com.diego.escuela.repositories.CalificacionRepository;
import com.diego.escuela.repositories.GrupoRepository;
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
public class InscripcionServiceImpl implements InscripcionService {
    private final InscripcionRepository inscripcionRepository;
    private final AlumnoRepository alumnoRepository;
    private final GrupoRepository grupoRepository;
    private final CalificacionRepository calificacionRepository;
    private final InscripcionMapper inscripcionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listar() {
        return inscripcionRepository.findAll().stream()
                .map(inscripcionMapper::responseAEntidad)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionResponse obtenerPorId(Long id) {
        return inscripcionMapper.responseAEntidad(obtenerInscripcion(id));
    }

    @Override
    public InscripcionResponse registrar(InscripcionRequest request) {
        Alumno alumno = obtenerAlumno(request.idAlumno());
        Grupo grupo = obtenerGrupo(request.idGrupo());
        validarUnicidad(alumno.getId(), grupo.getId(), null);

        Inscripcion guardada = inscripcionRepository.save(Inscripcion.crear(alumno, grupo));
        log.info("Inscripción registrada con id {}", guardada.getId());
        return inscripcionMapper.responseAEntidad(guardada);
    }

    @Override
    public InscripcionResponse actualizar(InscripcionRequest request, Long id) {
        Inscripcion inscripcion = obtenerInscripcion(id);
        Alumno alumno = obtenerAlumno(request.idAlumno());
        Grupo grupo = obtenerGrupo(request.idGrupo());
        validarUnicidad(alumno.getId(), grupo.getId(), id);

        inscripcion.actualizar(alumno, grupo);
        Inscripcion actualizada = inscripcionRepository.save(inscripcion);
        log.info("Inscripción actualizada con id {}", actualizada.getId());
        return inscripcionMapper.responseAEntidad(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Inscripcion inscripcion = obtenerInscripcion(id);
        if (calificacionRepository.existePorInscripcion(id)) {
            throw new EntidadRelacionadaException(
                    "No se puede eliminar la inscripción porque tiene una calificación asociada."
            );
        }

        inscripcionRepository.delete(inscripcion);
        log.info("Inscripción eliminada con id {}", id);
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOException(inscripcionRepository, id, Inscripcion.class);
    }

    private Alumno obtenerAlumno(Long id) {
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, id, Alumno.class);
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }

    private void validarUnicidad(Long idAlumno, Long idGrupo, Long idInscripcion) {
        boolean existe = idInscripcion == null
                ? inscripcionRepository.existePorAlumnoYGrupo(idAlumno, idGrupo)
                : inscripcionRepository.existePorAlumnoYGrupoExcluyendo(idAlumno, idGrupo, idInscripcion);
        if (existe) {
            throw new ConflictoException("El alumno ya está inscrito en ese grupo.");
        }
    }
}
