package com.diego.escuela.services.alumnos;

import com.diego.escuela.dto.alumnos.AlumnoRequest;
import com.diego.escuela.dto.alumnos.AlumnoResponse;
import com.diego.escuela.entities.Alumno;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.AlumnoMapper;
import com.diego.escuela.repositories.AlumnoRepository;
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

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> listar() {
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
        String matricula = alumnoRepository.generarMatricula(
                request.nombre(), request.apellidoPaterno(), request.apellidoMaterno()
        );
        String email = alumnoRepository.generarEmail(
                request.nombre(), request.apellidoPaterno(), request.apellidoMaterno()
        );
        Alumno guardado = alumnoRepository.save(
                alumnoMapper.crearEntidad(request, matricula, email)
        );
        log.info("Alumno {} registrado con id {}", guardado.getMatricula(), guardado.getId());
        return alumnoMapper.responseAEntidad(guardado);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, Long id) {
        Alumno alumno = obtenerAlumno(id);
        boolean cambiaronDatosPersonales =
                !alumno.getNombre().equals(request.nombre().trim())
                        || !alumno.getApellidoPaterno().equals(request.apellidoPaterno().trim())
                        || !alumno.getApellidoMaterno().equals(request.apellidoMaterno().trim());

        String matricula = alumno.getMatricula();
        String email = alumno.getEmail();
        if (cambiaronDatosPersonales) {
            matricula = alumnoRepository.generarMatricula(
                    request.nombre(), request.apellidoPaterno(), request.apellidoMaterno()
            );
            email = alumnoRepository.generarEmail(
                    request.nombre(), request.apellidoPaterno(), request.apellidoMaterno()
            );
        }

        alumno.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                matricula,
                email
        );
        log.info("Alumno actualizado con id {}", alumno.getId());
        return alumnoMapper.responseAEntidad(alumno);
    }

    @Override
    public void eliminar(Long id) {
        Alumno alumno = obtenerAlumno(id);
        if (alumnoRepository.tieneInscripciones(id)) {
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el alumno porque tiene inscripciones asociadas."
            );
        }

        alumnoRepository.delete(alumno);
        log.info("Alumno eliminado con id {}", id);
    }

    private Alumno obtenerAlumno(Long id) {
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, id, Alumno.class);
    }
}
