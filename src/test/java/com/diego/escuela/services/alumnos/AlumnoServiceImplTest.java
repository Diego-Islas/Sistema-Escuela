package com.diego.escuela.services.alumnos;

import com.diego.escuela.dto.alumnos.AlumnoRequest;
import com.diego.escuela.entities.Alumno;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.AlumnoMapper;
import com.diego.escuela.repositories.AlumnoRepository;
import com.diego.escuela.repositories.InscripcionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceImplTest {
    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private AlumnoMapper alumnoMapper;
    @Mock
    private InscripcionRepository inscripcionRepository;

    @InjectMocks
    private AlumnoServiceImpl service;

    @Test
    void registrarSolicitaMatriculaYCorreoAFuncionesDeOracle() {
        AlumnoRequest request = new AlumnoRequest("Carlos", "González", "Ramírez");
        Alumno alumno = Alumno.crear(
                "Carlos", "González", "Ramírez"
        );
        when(alumnoRepository.generarMatricula("Carlos", "González", "Ramírez"))
                .thenReturn("GORACA2601");
        when(alumnoRepository.generarEmail("Carlos", "González", "Ramírez"))
                .thenReturn("atr.26.carlos.gonzalez.ramirez.goraca2601@escuela.com.mx");
        when(alumnoMapper.requestAEntidad(
                request,
                "GORACA2601",
                "atr.26.carlos.gonzalez.ramirez.goraca2601@escuela.com.mx"
        )).thenReturn(alumno);
        when(alumnoRepository.saveAndFlush(alumno)).thenReturn(alumno);

        service.registrar(request);

        verify(alumnoRepository).generarMatricula("Carlos", "González", "Ramírez");
        verify(alumnoRepository).generarEmail("Carlos", "González", "Ramírez");
        verify(alumnoRepository).saveAndFlush(alumno);
        verify(alumnoMapper).responseAEntidad(alumno);
    }

    @Test
    void actualizarRegeneraDatosAcademicosSoloCuandoCambianDatosPersonales() {
        Alumno alumno = Alumno.builder()
                .id(1L)
                .nombre("Carlos")
                .apellidoPaterno("Pérez")
                .apellidoMaterno("López")
                .matricula("PELOCA2601")
                .email("carlos@escuela.com")
                .build();
        AlumnoRequest request = new AlumnoRequest("Carlos", "Gómez", "López");
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(alumnoRepository.generarMatricula("Carlos", "Gómez", "López")).thenReturn("GOMECA2602");
        when(alumnoRepository.generarEmail("Carlos", "Gómez", "López")).thenReturn("carlos.gomez@escuela.com");

        service.actualizar(request, 1L);

        verify(alumnoRepository).generarMatricula("Carlos", "Gómez", "López");
        verify(alumnoRepository).generarEmail("Carlos", "Gómez", "López");
        verify(alumnoRepository).saveAndFlush(alumno);
    }

    @Test
    void actualizarSinCambiosNoRegeneraNiGuarda() {
        Alumno alumno = Alumno.builder()
                .id(1L)
                .nombre("Carlos")
                .apellidoPaterno("Pérez")
                .apellidoMaterno("López")
                .matricula("PELOCA2601")
                .email("carlos@escuela.com")
                .build();
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

        service.actualizar(new AlumnoRequest("Carlos", "Pérez", "López"), 1L);

        verify(alumnoRepository, never()).generarMatricula(any(), any(), any());
        verify(alumnoRepository, never()).generarEmail(any(), any(), any());
        verify(alumnoRepository, never()).saveAndFlush(any(Alumno.class));
        verify(alumnoMapper).responseAEntidad(alumno);
    }

    @Test
    void eliminarRechazaAlumnoConInscripciones() {
        Alumno alumno = Alumno.builder().id(3L).build();
        when(alumnoRepository.findById(3L)).thenReturn(Optional.of(alumno));
        when(inscripcionRepository.existsByAlumnoId(3L)).thenReturn(true);

        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(3L));

        verify(alumnoRepository, never()).delete(alumno);
    }

    @Test
    void eliminarBorraAlumnoSinInscripciones() {
        Alumno alumno = Alumno.builder().id(3L).build();
        when(alumnoRepository.findById(3L)).thenReturn(Optional.of(alumno));
        when(inscripcionRepository.existsByAlumnoId(3L)).thenReturn(false);

        service.eliminar(3L);

        verify(alumnoRepository).delete(alumno);
        verify(alumnoRepository).flush();
    }

    @Test
    void listarYObtenerPorIdConsultanRepositorioYMapeanRespuesta() {
        Alumno alumno = Alumno.builder().id(1L).build();
        when(alumnoRepository.findAll()).thenReturn(List.of(alumno));
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

        service.listar();
        service.obtenerPorId(1L);

        verify(alumnoMapper, org.mockito.Mockito.times(2)).responseAEntidad(alumno);
    }
}
