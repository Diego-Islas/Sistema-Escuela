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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        assertThrows(
                ConflictoException.class,
                () -> service.registrar(new InscripcionRequest(10L, 5L))
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
}
