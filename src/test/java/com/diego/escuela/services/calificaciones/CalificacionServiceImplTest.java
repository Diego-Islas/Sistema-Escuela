package com.diego.escuela.services.calificaciones;

import com.diego.escuela.dto.calificaciones.CalificacionRequest;
import com.diego.escuela.entities.Inscripcion;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.mapper.CalificacionMapper;
import com.diego.escuela.repositories.CalificacionRepository;
import com.diego.escuela.repositories.InscripcionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceImplTest {
    @Mock
    private CalificacionRepository calificacionRepository;
    @Mock
    private InscripcionRepository inscripcionRepository;
    @Mock
    private CalificacionMapper calificacionMapper;

    @InjectMocks
    private CalificacionServiceImpl service;

    @Test
    void registrarRechazaSegundaCalificacionParaInscripcion() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        when(inscripcionRepository.findById(15L)).thenReturn(Optional.of(inscripcion));
        when(calificacionRepository.existsByInscripcionId(15L)).thenReturn(true);

        assertThrows(
                ConflictoException.class,
                () -> service.registrar(new CalificacionRequest(15L, new BigDecimal("8.5")))
        );

        verify(calificacionRepository, never()).saveAndFlush(any());
    }
}
