package com.diego.escuela.services.cursos;

import com.diego.escuela.dto.cursos.CursoRequest;
import com.diego.escuela.dto.cursos.CursoResponse;
import com.diego.escuela.entities.Curso;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.mapper.CursoMapper;
import com.diego.escuela.repositories.CursoRepository;
import com.diego.escuela.repositories.GrupoRepository;
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
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final GrupoRepository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {
        log.info("Iniciando consulta de todos los cursos");

        return cursoRepository.findAll()
                .stream()
                .map(cursoMapper::responseAEntidad)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerPorId(Long id) {
        log.info("Consultando curso por id {}", id);

        return cursoMapper.responseAEntidad(obtenerCurso(id));
    }

    @Override
    public CursoResponse registrar(CursoRequest request) {
        log.info("Iniciando registro de curso");

        Curso curso = cursoMapper.requestAEntidad(request);
        validarUnicidad(curso.getNombre());
        Curso guardado = cursoRepository.saveAndFlush(curso);

        log.info("Curso registrado correctamente con id {}", guardado.getId());

        return cursoMapper.responseAEntidad(guardado);
    }

    @Override
    public CursoResponse actualizar(CursoRequest request, Long id) {
        log.info("Iniciando actualización del curso");

        Curso curso = obtenerCurso(id);

        Curso datos = cursoMapper.requestAEntidad(request);
        validarUnicidad(datos.getNombre(), id);
        curso.actualizar(datos.getNombre(), datos.getDescripcion(), datos.getCreditos());

        Curso actualizado = cursoRepository.saveAndFlush(curso);

        log.info("Curso actualizado correctamente con id {}", actualizado.getId());

        return cursoMapper.responseAEntidad(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Iniciando eliminación del curso por id {}", id);

        Curso curso = obtenerCurso(id);

        if (grupoRepository.existsByCursoId(id)) {
            throw new com.diego.escuela.exceptions.EntidadRelacionadaException(
                    "No se puede eliminar el curso porque tiene grupos asignados."
            );
        }

        cursoRepository.delete(curso);
        cursoRepository.flush();
        log.info("Curso eliminado correctamente con id {}", id);
    }

    private Curso obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                cursoRepository,
                id,
                Curso.class
        );
    }

    private void validarUnicidad(String nombre) {
        if (cursoRepository.existsByNombre(nombre)) {
            throw new ConflictoException(
                    "Ya existe un curso registrado con ese nombre."
            );
        }
    }

    private void validarUnicidad(String nombre, Long id) {
        if (cursoRepository.existsByNombreAndIdNot(nombre, id)) {
            throw new ConflictoException(
                    "Ya existe otro curso registrado con ese nombre."
            );
        }
    }
}