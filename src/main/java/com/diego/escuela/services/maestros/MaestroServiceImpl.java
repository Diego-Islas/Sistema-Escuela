package com.diego.escuela.services.maestros;

import com.diego.escuela.dto.datos.DatosCurso;
import com.diego.escuela.dto.maestros.MaestroRequest;
import com.diego.escuela.dto.maestros.MaestroResponse;
import com.diego.escuela.entities.Aula;
import com.diego.escuela.entities.Curso;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Maestro;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.CursoMapper;
import com.diego.escuela.mapper.MaestroMapper;
import com.diego.escuela.repositories.CursoRepository;
import com.diego.escuela.repositories.GrupoRepository;
import com.diego.escuela.repositories.MaestroRepository;
import com.diego.escuela.utils.ServiceUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MaestroServiceImpl implements MaestroService {

    private final MaestroRepository maestroRepository;
    private final MaestroMapper maestroMapper;

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    private final GrupoRepository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listar() {
        log.info("Listando maestros");
        return maestroRepository.findAll().stream()
                .map(maestroMapper::responseAEntidad).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaestroResponse obtenerPorId(Long id) {
        return maestroMapper.responseAEntidad(obterMaestro(id));
    }

    @Override
    public MaestroResponse registrar(MaestroRequest request) {

        Maestro maestro = Maestro.crearMaestro(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono()
        );

        validarDatosUnicos(maestro, null);

        maestroRepository.saveAndFlush(maestro);
        log.info("Maestro {} agregado con id: {}", maestro.getNombre(), maestro.getId());
        return maestroMapper.responseAEntidad(maestro);
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {
        Maestro maestro = obterMaestro(id);

        maestro.actualizarMaestro(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono()
        );

        validarDatosUnicos(maestro, id);

        maestroRepository.saveAndFlush(maestro);

        log.info("Maestro {} actualizado con id: {}", maestro.getNombre(), maestro.getId());

        return maestroMapper.responseAEntidad(maestro);
    }

    @Override
    public void eliminar(Long id) {
        Maestro maestro = obterMaestro(id);

        // Validar si el maestro tiene grupos asignados antes de eliminarlo
        if (grupoRepository.existsByMaestroId(id))
            throw new EntidadRelacionadaException("No es posible eliminar el maestro con id " + id + " porque tiene grupos asignados.");

        maestroRepository.delete(maestro);
        maestroRepository.flush();

        log.info("Maestro eliminado con id: {}", id);
    }

    @Transactional(readOnly = true)
    public List<DatosCurso> obtenerCursosDeUnMaestroConId(Long id) {
        obterMaestro(id);

        log.info("Listando cursos del maestro con id: {}", id);

        return cursoRepository.obtenerCursosPorIdMaestro(id).stream()
                .map(cursoMapper::entidadADatosCurso).toList();
    }

    private Maestro obterMaestro(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                maestroRepository,
                id,
                Maestro.class
        );
    }

    private void validarDatosUnicos(Maestro maestro, Long id) {
        boolean emailDuplicado = id == null
                ? maestroRepository.existsByEmail(maestro.getEmail())
                : maestroRepository.existsByEmailAndIdNot(maestro.getEmail(), id);
        if (emailDuplicado)
            throw new ConflictoException("Email ya existente");

        boolean telefonoDuplicado = id == null
                ? maestroRepository.existsByTelefono(maestro.getTelefono())
                : maestroRepository.existsByTelefonoAndIdNot(maestro.getTelefono(), id);
        if (telefonoDuplicado)
            throw new ConflictoException("Teléfono ya existente");
    }

}
