package com.diego.escuela.services.inscripciones;

import com.diego.escuela.dto.inscripciones.InscripcionRequest;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceImplTest {
    @Mock
    private InscripcionRepository inscripcionRepository;
    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private CalificacionRepository calificacionRepository;
    @Mock
    private InscripcionMapper inscripcionMapper;

    @InjectMocks
    private InscripcionServiceImpl service;

    @Test
    void registrarRechazaAlumnoInscritoAlMismoGrupo() {
        Alumno alumno = Alumno.builder().id(10L).build();
        Grupo grupo = Grupo.builder().id(5L).build();
        when(alumnoRepository.findById(10L)).thenReturn(Optional.of(alumno));
        when(grupoRepository.findById(5L)).thenReturn(Optional.of(grupo));
        when(inscripcionRepository.existsByAlumnoIdAndGrupoId(10L, 5L)).thenReturn(true);

        InscripcionRequest request = new InscripcionRequest(10L, 5L);

        assertThrows(
                ConflictoException.class,
                () -> service.registrar(request)
        );

        verify(inscripcionRepository, never()).saveAndFlush(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void eliminarRechazaInscripcionConCalificacion() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        when(inscripcionRepository.findById(15L)).thenReturn(Optional.of(inscripcion));
        when(calificacionRepository.existsByInscripcionId(15L)).thenReturn(true);

        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(15L));

        verify(inscripcionRepository, never()).delete(inscripcion);
    }

    @Test
    void registrarCreaInscripcionCuandoNoExisteDuplicada() {
        Alumno alumno = Alumno.builder().id(10L).build();
        Grupo grupo = Grupo.builder().id(5L).build();
        when(alumnoRepository.findById(10L)).thenReturn(Optional.of(alumno));
        when(grupoRepository.findById(5L)).thenReturn(Optional.of(grupo));
        when(inscripcionRepository.existsByAlumnoIdAndGrupoId(10L, 5L)).thenReturn(false);
        when(inscripcionRepository.saveAndFlush(any(Inscripcion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.registrar(new InscripcionRequest(10L, 5L));

        verify(inscripcionRepository).saveAndFlush(any(Inscripcion.class));
    }

    @Test
    void actualizarRechazaCombinacionAlumnoGrupoDuplicada() {
        Inscripcion actual = Inscripcion.builder().id(15L).build();
        when(inscripcionRepository.findById(15L)).thenReturn(Optional.of(actual));
        when(alumnoRepository.findById(10L)).thenReturn(Optional.of(Alumno.builder().id(10L).build()));
        when(grupoRepository.findById(5L)).thenReturn(Optional.of(Grupo.builder().id(5L).build()));
        when(inscripcionRepository.existsByAlumnoIdAndGrupoIdAndIdNot(10L, 5L, 15L)).thenReturn(true);

        InscripcionRequest request = new InscripcionRequest(10L, 5L);

        assertThrows(
                ConflictoException.class,
                () -> service.actualizar(request, 15L)
        );

        verify(inscripcionRepository, never()).saveAndFlush(any(Inscripcion.class));
    }

    @Test
    void actualizarGuardaInscripcionSinDuplicarLaCombinacion() {
        Inscripcion actual = Inscripcion.builder().id(15L).build();
        Alumno alumno = Alumno.builder().id(10L).build();
        Grupo grupo = Grupo.builder().id(5L).build();
        when(inscripcionRepository.findById(15L)).thenReturn(Optional.of(actual));
        when(alumnoRepository.findById(10L)).thenReturn(Optional.of(alumno));
        when(grupoRepository.findById(5L)).thenReturn(Optional.of(grupo));
        when(inscripcionRepository.existsByAlumnoIdAndGrupoIdAndIdNot(10L, 5L, 15L))
                .thenReturn(false);
        when(inscripcionRepository.saveAndFlush(actual)).thenReturn(actual);

        service.actualizar(new InscripcionRequest(10L, 5L), 15L);

        org.junit.jupiter.api.Assertions.assertSame(alumno, actual.getAlumno());
        org.junit.jupiter.api.Assertions.assertSame(grupo, actual.getGrupo());
        verify(inscripcionRepository).saveAndFlush(actual);
    }

    @Test
    void eliminarBorraInscripcionSinCalificacion() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        when(inscripcionRepository.findById(15L)).thenReturn(Optional.of(inscripcion));
        when(calificacionRepository.existsByInscripcionId(15L)).thenReturn(false);

        service.eliminar(15L);

        verify(inscripcionRepository).delete(inscripcion);
        verify(inscripcionRepository).flush();
    }

    @Test
    void listarYObtenerPorIdConsultanRepositorio() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        when(inscripcionRepository.findAll()).thenReturn(List.of(inscripcion));
        when(inscripcionRepository.findById(15L)).thenReturn(Optional.of(inscripcion));

        service.listar();
        service.obtenerPorId(15L);

        verify(inscripcionMapper, times(2)).responseAEntidad(inscripcion);
    }
}
