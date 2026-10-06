package com.diego.escuela.services.alumnos;

import com.diego.escuela.dto.alumnos.AlumnoRequest;
import com.diego.escuela.dto.alumnos.AlumnoResponse;
import com.diego.escuela.entities.Alumno;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.AlumnoMapper;
import com.diego.escuela.repositories.AlumnoRepository;
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
public class AlumnoServiceImpl implements AlumnoService {
    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper alumnoMapper;
    private final InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> listar() {
        log.info("Obteniendo lista de alumnos");
        return alumnoRepository.findAll().stream()
                .map(alumnoMapper::responseAEntidad)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse obtenerPorId(Long id) {
        return alumnoMapper.responseAEntidad(obtenerAlumno(id));
    }

    @Override
    public AlumnoResponse registrar(AlumnoRequest request) {
        log.info("Registrando alumno: {} {} {}", request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());

        Alumno alumno = alumnoMapper.requestAEntidad(request, generarMatricula(request), generarEmail(request));
        alumnoRepository.saveAndFlush(alumno);

        log.info("Alumno registrado con id {}", alumno.getId());
        return alumnoMapper.responseAEntidad(alumno);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, Long id) {
        Alumno alumno = obtenerAlumno(id);

        if (alumno.cambioEnDatosPersonales(request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno())) {
            log.info("Actualizando alumno con id {}", id);

            alumno.actualizar(
                    request.nombre(),
                    request.apellidoPaterno(),
                    request.apellidoMaterno(),
                    generarMatricula(request),
                    generarEmail(request)
            );

            alumnoRepository.saveAndFlush(alumno);
            log.info("Alumno actualizado con id {}", alumno.getId());
        }

        return alumnoMapper.responseAEntidad(alumno);
    }

    @Override
    public void eliminar(Long id) {
        Alumno alumno = obtenerAlumno(id);
        log.info("Eliminando alumno con id {}", id);

        if (inscripcionRepository.existsByAlumnoId(id))
            throw new EntidadRelacionadaException("No se puede eliminar el alumno porque tiene inscripciones asociadas");

        alumnoRepository.delete(alumno);
        alumnoRepository.flush();
        log.info("Alumno eliminado con id {}", id);
    }

    private Alumno obtenerAlumno(Long id) {
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, id, Alumno.class);
    }

    private String generarMatricula(AlumnoRequest request) {
        log.info("Generando matrícula para: {} {} {}", request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());
        return alumnoRepository.generarMatricula(request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());
    }

    private String generarEmail(AlumnoRequest request) {
        log.info("Generando email para: {} {} {}", request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());
        return alumnoRepository.generarEmail(request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());
    }
}
