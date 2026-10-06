package com.diego.escuela.services.horarios;

import com.diego.escuela.dto.horarios.HorarioRequest;
import com.diego.escuela.dto.horarios.HorarioResponse;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Horario;
import com.diego.escuela.enums.DiaSemana;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.DatoInvalidoException;
import com.diego.escuela.mapper.HorarioMapper;
import com.diego.escuela.repositories.GrupoRepository;
import com.diego.escuela.repositories.HorarioRepository;
import com.diego.escuela.utils.HoraUtils;
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
public class HorarioServiceImpl implements HorarioService {
    private final HorarioRepository horarioRepository;
    private final GrupoRepository grupoRepository;
    private final HorarioMapper horarioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponse> listar() {
        return horarioRepository.findAll().stream()
                .map(horarioMapper::responseAEntidad)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioResponse obtenerPorId(Long id) {
        return horarioMapper.responseAEntidad(obtenerHorario(id));
    }

    @Override
    public HorarioResponse registrar(HorarioRequest request) {
        Grupo grupo = obtenerGrupo(request.idGrupo());
        DiaSemana dia = DiaSemana.obtenerDiaPorDescripcion(request.dia());
        validarHoras(request.horaInicio(), request.horaFin());
        validarSinTraslape(null, grupo, dia, request.horaInicio(), request.horaFin());

        Horario horario = Horario.crear(
                grupo, dia, request.horaInicio(), request.horaFin()
        );
        Horario guardado = horarioRepository.save(horario);
        log.info("Horario registrado con id {}", guardado.getId());
        return horarioMapper.responseAEntidad(guardado);
    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, Long id) {
        Horario horario = obtenerHorario(id);
        Grupo grupo = obtenerGrupo(request.idGrupo());
        DiaSemana dia = DiaSemana.obtenerDiaPorDescripcion(request.dia());
        validarHoras(request.horaInicio(), request.horaFin());
        validarSinTraslape(id, grupo, dia, request.horaInicio(), request.horaFin());

        horario.actualizar(grupo, dia, request.horaInicio(), request.horaFin());
        Horario actualizado = horarioRepository.save(horario);
        log.info("Horario actualizado con id {}", actualizado.getId());
        return horarioMapper.responseAEntidad(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Horario horario = obtenerHorario(id);
        horarioRepository.delete(horario);
        log.info("Horario eliminado con id {}", id);
    }

    private Horario obtenerHorario(Long id) {
        return ServiceUtils.obtenerEntidadOException(horarioRepository, id, Horario.class);
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }

    private void validarHoras(String horaInicio, String horaFin) {
        HoraUtils.validarHora(horaInicio, "La hora de inicio debe tener formato HH:mm");
        HoraUtils.validarHora(horaFin, "La hora de fin debe tener formato HH:mm");
        if (!HoraUtils.esHoraPosterior(horaFin, horaInicio)) {
            throw new DatoInvalidoException("La hora de fin debe ser posterior a la hora de inicio");
        }
    }

    private void validarSinTraslape(
            Long idHorario,
            Grupo grupo,
            DiaSemana dia,
            String horaInicio,
            String horaFin
    ) {
        boolean existeTraslape = idHorario == null
                ? horarioRepository.existeTraslape(
                dia, grupo.getId(), grupo.getAula().getId(), horaInicio, horaFin
        )
                : horarioRepository.existeTraslapeExcluyendoHorario(
                idHorario, dia, grupo.getId(), grupo.getAula().getId(), horaInicio, horaFin
        );

        if (existeTraslape) {
            throw new ConflictoException(
                    "El horario se traslapa con otro horario del mismo grupo o aula."
            );
        }
    }
}
