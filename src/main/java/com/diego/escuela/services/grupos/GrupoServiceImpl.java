package com.diego.escuela.services.grupos;

import com.diego.escuela.dto.grupos.GrupoRequest;
import com.diego.escuela.dto.grupos.GrupoResponse;
import com.diego.escuela.entities.Aula;
import com.diego.escuela.entities.Curso;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Maestro;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.GrupoMapper;
import com.diego.escuela.repositories.AulaRepository;
import com.diego.escuela.repositories.CursoRepository;
import com.diego.escuela.repositories.GrupoRepository;
import com.diego.escuela.repositories.MaestroRepository;
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
public class GrupoServiceImpl implements GrupoService {
    private final GrupoRepository grupoRepository;
    private final CursoRepository cursoRepository;
    private final MaestroRepository maestroRepository;
    private final AulaRepository aulaRepository;
    private final GrupoMapper grupoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> listar() {
        return grupoRepository.findAll().stream()
                .map(grupoMapper::responseAEntidad)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerPorId(Long id) {
        return grupoMapper.responseAEntidad(obtenerGrupo(id));
    }

    @Override
    public GrupoResponse registrar(GrupoRequest request) {
        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obtenerMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());
        validarUnicidad(curso.getId(), maestro.getId(), aula.getId(), request.periodo(), null);

        Grupo guardado = grupoRepository.save(
                Grupo.crear(curso, maestro, aula, request.periodo())
        );
        log.info("Grupo registrado con id {}", guardado.getId());
        return grupoMapper.responseAEntidad(guardado);
    }

    @Override
    public GrupoResponse actualizar(GrupoRequest request, Long id) {
        Grupo grupo = obtenerGrupo(id);
        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obtenerMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());
        validarUnicidad(curso.getId(), maestro.getId(), aula.getId(), request.periodo(), id);

        grupo.actualizar(curso, maestro, aula, request.periodo());
        Grupo actualizado = grupoRepository.save(grupo);
        log.info("Grupo actualizado con id {}", actualizado.getId());
        return grupoMapper.responseAEntidad(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Grupo grupo = obtenerGrupo(id);
        if (!grupo.getInscripciones().isEmpty() || !grupo.getHorarios().isEmpty()) {
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo porque tiene inscripciones u horarios asociados."
            );
        }

        grupoRepository.delete(grupo);
        log.info("Grupo eliminado con id {}", id);
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }

    private Curso obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOException(cursoRepository, id, Curso.class);
    }

    private Maestro obtenerMaestro(Long id) {
        return ServiceUtils.obtenerEntidadOException(maestroRepository, id, Maestro.class);
    }

    private Aula obtenerAula(Long id) {
        return ServiceUtils.obtenerEntidadOException(aulaRepository, id, Aula.class);
    }

    private void validarUnicidad(
            Long idCurso,
            Long idMaestro,
            Long idAula,
            String periodo,
            Long idGrupo
    ) {
        boolean existe = idGrupo == null
                ? grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodo(
                        idCurso, idMaestro, idAula, periodo
                )
                : grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(
                        idCurso, idMaestro, idAula, periodo, idGrupo
                );
        if (existe) {
            throw new ConflictoException(
                    "Ya existe un grupo con el mismo curso, maestro, aula y periodo."
            );
        }
    }
}
