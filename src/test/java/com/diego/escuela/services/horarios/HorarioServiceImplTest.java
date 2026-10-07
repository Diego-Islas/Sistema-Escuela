package com.diego.escuela.services.horarios;

import com.diego.escuela.dto.horarios.HorarioRequest;
import com.diego.escuela.entities.Aula;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Horario;
import com.diego.escuela.enums.DiaSemana;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.DatoInvalidoException;
import com.diego.escuela.mapper.HorarioMapper;
import com.diego.escuela.repositories.GrupoRepository;
import com.diego.escuela.repositories.HorarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HorarioServiceImplTest {

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private HorarioMapper horarioMapper;

    @InjectMocks
    private HorarioServiceImpl service;

    @Test
    void registrarRechazaTraslapeDeGrupoOAula() {
        Grupo grupo = grupoConAula();

        when(grupoRepository.findById(1L)).thenReturn(Optional.of(grupo));

        when(horarioRepository.existeTraslape(
                null,
                DiaSemana.LUNES,
                1L,
                3L,
                "08:00",
                "10:00"
        )).thenReturn(true);

        HorarioRequest request =
                new HorarioRequest(1L, "Lunes", "08:00", "10:00");

        assertThrows(
                ConflictoException.class,
                () -> service.registrar(request)
        );

        verify(horarioRepository, never())
                .saveAndFlush(any(Horario.class));
    }

    @Test
    void registrarGuardaHorarioSinTraslape() {
        Grupo grupo = grupoConAula();

        when(grupoRepository.findById(1L)).thenReturn(Optional.of(grupo));

        when(horarioRepository.existeTraslape(
                null,
                DiaSemana.LUNES,
                1L,
                3L,
                "08:00",
                "10:00"
        )).thenReturn(false);

        when(horarioRepository.saveAndFlush(any(Horario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.registrar(
                new HorarioRequest(1L, "Lunes", "08:00", "10:00")
        );

        verify(horarioRepository)
                .saveAndFlush(any(Horario.class));
    }

    @Test
    void registrarRechazaFormatoDeHoraInvalidoAntesDeConsultarTraslapes() {
        when(grupoRepository.findById(1L))
                .thenReturn(Optional.of(grupoConAula()));

        HorarioRequest request =
                new HorarioRequest(1L, "Lunes", "8:00", "10:00");

        assertThrows(
                DatoInvalidoException.class,
                () -> service.registrar(request)
        );

        verify(horarioRepository, never())
                .existeTraslape(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                );

        verify(horarioRepository, never())
                .saveAndFlush(any(Horario.class));
    }

    @Test
    void actualizarValidaTraslapeExcluyendoElHorarioActual() {
        Horario horario = Horario.builder()
                .id(8L)
                .grupo(grupoConAula())
                .dia(DiaSemana.LUNES)
                .horaInicio("08:00")
                .horaFin("10:00")
                .build();

        when(horarioRepository.findById(8L))
                .thenReturn(Optional.of(horario));

        when(grupoRepository.findById(1L))
                .thenReturn(Optional.of(grupoConAula()));

        when(horarioRepository.existeTraslape(
                8L,
                DiaSemana.MARTES,
                1L,
                3L,
                "10:00",
                "12:00"
        )).thenReturn(false);

        when(horarioRepository.saveAndFlush(horario))
                .thenReturn(horario);

        service.actualizar(
                new HorarioRequest(1L, "Martes", "10:00", "12:00"),
                8L
        );

        verify(horarioRepository).existeTraslape(
                8L,
                DiaSemana.MARTES,
                1L,
                3L,
                "10:00",
                "12:00"
        );

        verify(horarioRepository)
                .saveAndFlush(horario);
    }

    @Test
    void actualizarRechazaTraslapeConOtroHorario() {
        Horario horario = Horario.builder()
                .id(8L)
                .grupo(grupoConAula())
                .dia(DiaSemana.LUNES)
                .horaInicio("08:00")
                .horaFin("10:00")
                .build();

        when(horarioRepository.findById(8L))
                .thenReturn(Optional.of(horario));

        when(grupoRepository.findById(1L))
                .thenReturn(Optional.of(grupoConAula()));

        when(horarioRepository.existeTraslape(
                8L,
                DiaSemana.MARTES,
                1L,
                3L,
                "10:00",
                "12:00"
        )).thenReturn(true);

        HorarioRequest request =
                new HorarioRequest(1L, "Martes", "10:00", "12:00");

        assertThrows(
                ConflictoException.class,
                () -> service.actualizar(request, 8L)
        );

        verify(horarioRepository, never())
                .saveAndFlush(any(Horario.class));
    }

    @Test
    void eliminarBorraHorario() {
        Horario horario = Horario.builder()
                .id(8L)
                .build();

        when(horarioRepository.findById(8L))
                .thenReturn(Optional.of(horario));

        service.eliminar(8L);

        verify(horarioRepository).delete(horario);
        verify(horarioRepository).flush();
    }

    private Grupo grupoConAula() {
        return Grupo.builder()
                .id(1L)
                .aula(
                        Aula.builder()
                                .id(3L)
                                .nombre("Aula 101")
                                .capacidad(30)
                                .build()
                )
                .build();
    }

    @Test
    void listarYObtenerPorIdConsultanRepositorio() {
        Horario horario = Horario.builder()
                .id(8L)
                .build();

        when(horarioRepository.findAll())
                .thenReturn(List.of(horario));

        when(horarioRepository.findById(8L))
                .thenReturn(Optional.of(horario));

        service.listar();
        service.obtenerPorId(8L);

        verify(horarioMapper, times(2))
                .responseAEntidad(horario);
    }
}