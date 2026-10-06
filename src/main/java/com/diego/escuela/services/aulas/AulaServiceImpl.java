package com.diego.escuela.services.aulas;

import com.diego.escuela.dto.aulas.AulaRequest;
import com.diego.escuela.dto.aulas.AulaResponse;
import com.diego.escuela.entities.Aula;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.AulaMapper;
import com.diego.escuela.repositories.AulaRepository;
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
public class AulaServiceImpl implements AulaService {
    private final AulaRepository aulaRepository;
    private final AulaMapper aulaMapper;
    private final GrupoRepository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AulaResponse> listar() {
        return aulaRepository.findAll().stream()
                .map(aulaMapper::responseAEntidad)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AulaResponse obtenerPorId(Long id) {
        return aulaMapper.responseAEntidad(obtenerAula(id));
    }

    @Override
    public AulaResponse registrar(AulaRequest request) {
        Aula aula = aulaMapper.requestAEntidad(request);
        validarNombreUnico(aula.getNombre(), null);

        Aula guardada = aulaRepository.save(aula);
        log.info("Aula {} registrada con id {}", guardada.getNombre(), guardada.getId());
        return aulaMapper.responseAEntidad(guardada);
    }

    @Override
    public AulaResponse actualizar(AulaRequest request, Long id) {
        Aula aula = obtenerAula(id);
        Aula datos = aulaMapper.requestAEntidad(request);
        validarNombreUnico(datos.getNombre(), id);

        aula.actualizar(datos.getNombre(), datos.getCapacidad());
        Aula actualizada = aulaRepository.save(aula);
        log.info("Aula {} actualizada con id {}", actualizada.getNombre(), actualizada.getId());
        return aulaMapper.responseAEntidad(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        Aula aula = obtenerAula(id);
        if (grupoRepository.existsByAulaId(id)) {
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el aula porque tiene grupos asignados."
            );
        }

        aulaRepository.delete(aula);
        log.info("Aula eliminada con id {}", id);
    }

    private Aula obtenerAula(Long id) {
        return ServiceUtils.obtenerEntidadOException(aulaRepository, id, Aula.class);
    }

    private void validarNombreUnico(String nombre, Long id) {
        boolean existe = id == null
                ? aulaRepository.existsByNombre(nombre)
                : aulaRepository.existsByNombreAndIdNot(nombre, id);
        if (existe) {
            throw new ConflictoException("Ya existe un aula registrada con ese nombre.");
        }
    }
}
