package com.diego.escuela.services.alumnos;

import com.diego.escuela.dto.alumnos.AlumnoRequest;
import com.diego.escuela.entities.Alumno;
import com.diego.escuela.mapper.AlumnoMapper;
import com.diego.escuela.repositories.AlumnoRepository;
import com.diego.escuela.repositories.InscripcionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
