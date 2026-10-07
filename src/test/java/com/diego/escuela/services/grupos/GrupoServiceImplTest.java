package com.diego.escuela.services.grupos;

import com.diego.escuela.dto.grupos.GrupoRequest;
import com.diego.escuela.dto.grupos.GrupoResponse;
import com.diego.escuela.entities.Aula;
import com.diego.escuela.entities.Curso;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.entities.Inscripcion;
import com.diego.escuela.entities.Maestro;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.GrupoMapper;
import com.diego.escuela.repositories.AulaRepository;
import com.diego.escuela.repositories.CursoRepository;
import com.diego.escuela.repositories.GrupoRepository;
import com.diego.escuela.repositories.MaestroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrupoServiceImplTest {
    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private CursoRepository cursoRepository;
    @Mock
    private MaestroRepository maestroRepository;
    @Mock
    private AulaRepository aulaRepository;
    @Mock
    private GrupoMapper grupoMapper;

    @InjectMocks
    private GrupoServiceImpl service;

    @Test
    void registrarGuardaGrupoCuandoLaCombinacionNoExiste() {
        GrupoRequest request = new GrupoRequest(2L, 7L, 3L, "2026-01");
        Curso curso = Curso.builder().id(2L).build();
        Maestro maestro = Maestro.builder().id(7L).build();
        Aula aula = Aula.builder().id(3L).build();
        GrupoResponse response = new GrupoResponse(10L, null, null, null, null, "2026-01");

        when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso));
        when(maestroRepository.findById(7L)).thenReturn(Optional.of(maestro));
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(aula));
        when(grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodo(
                2L, 7L, 3L, "2026-01"
        )).thenReturn(false);
        when(grupoRepository.saveAndFlush(any(Grupo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(grupoMapper.responseAEntidad(any(Grupo.class))).thenReturn(response);

        GrupoResponse actual = service.registrar(request);

        assertEquals(response, actual);
        verify(grupoRepository).saveAndFlush(any(Grupo.class));
    }

    @Test
    void registrarRechazaCombinacionDuplicada() {
        GrupoRequest request = new GrupoRequest(2L, 7L, 3L, "2026-01");
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(Curso.builder().id(2L).build()));
        when(maestroRepository.findById(7L)).thenReturn(Optional.of(Maestro.builder().id(7L).build()));
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(Aula.builder().id(3L).build()));
        when(grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodo(
                2L, 7L, 3L, "2026-01"
        )).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.registrar(request));

        verify(grupoRepository, never()).saveAndFlush(any(Grupo.class));
    }

    @Test
    void actualizarExcluyeAlGrupoActualAlVerificarUnicidad() {
        Grupo actual = Grupo.builder().id(10L).build();
        GrupoRequest request = new GrupoRequest(2L, 7L, 3L, "2026-01");
        when(grupoRepository.findById(10L)).thenReturn(Optional.of(actual));
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(Curso.builder().id(2L).build()));
        when(maestroRepository.findById(7L)).thenReturn(Optional.of(Maestro.builder().id(7L).build()));
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(Aula.builder().id(3L).build()));
        when(grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(
                2L, 7L, 3L, "2026-01", 10L
        )).thenReturn(false);
        when(grupoRepository.saveAndFlush(actual)).thenReturn(actual);

        service.actualizar(request, 10L);

        verify(grupoRepository).existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(
                2L, 7L, 3L, "2026-01", 10L
        );
        verify(grupoRepository).saveAndFlush(actual);
    }

    @Test
    void actualizarRechazaCombinacionDeOtroGrupo() {
        Grupo actual = Grupo.builder().id(10L).build();
        GrupoRequest request = new GrupoRequest(2L, 7L, 3L, "2026-01");
        when(grupoRepository.findById(10L)).thenReturn(Optional.of(actual));
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(Curso.builder().id(2L).build()));
        when(maestroRepository.findById(7L)).thenReturn(Optional.of(Maestro.builder().id(7L).build()));
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(Aula.builder().id(3L).build()));
        when(grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(
                2L, 7L, 3L, "2026-01", 10L
        )).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.actualizar(request, 10L));

        verify(grupoRepository, never()).saveAndFlush(any(Grupo.class));
    }

    @Test
    void eliminarRechazaGrupoConInscripciones() {
        Grupo grupo = Grupo.builder().id(10L)
                .inscripciones(List.of(Inscripcion.builder().build()))
                .build();
        when(grupoRepository.findById(10L)).thenReturn(Optional.of(grupo));

        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(10L));

        verify(grupoRepository, never()).delete(grupo);
    }

    @Test
    void eliminarRechazaGrupoConHorarios() {
        Grupo grupo = Grupo.builder().id(10L)
                .horarios(List.of(com.diego.escuela.entities.Horario.builder().build()))
                .build();
        when(grupoRepository.findById(10L)).thenReturn(Optional.of(grupo));

        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(10L));

        verify(grupoRepository, never()).delete(grupo);
    }

    @Test
    void eliminarBorraGrupoSinRelaciones() {
        Grupo grupo = Grupo.builder().id(10L).build();
        when(grupoRepository.findById(10L)).thenReturn(Optional.of(grupo));

        service.eliminar(10L);

        verify(grupoRepository).delete(grupo);
        verify(grupoRepository).flush();
    }

    @Test
    void listarYObtenerPorIdConsultanRepositorio() {
        Grupo grupo = Grupo.builder().id(10L).build();
        when(grupoRepository.findAll()).thenReturn(List.of(grupo));
        when(grupoRepository.findById(10L)).thenReturn(Optional.of(grupo));

        service.listar();
        service.obtenerPorId(10L);

        verify(grupoMapper, org.mockito.Mockito.times(2)).responseAEntidad(grupo);
    }
}
