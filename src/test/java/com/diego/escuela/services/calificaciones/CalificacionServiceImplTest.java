package com.diego.escuela.services.calificaciones;

import com.diego.escuela.dto.calificaciones.CalificacionRequest;
import com.diego.escuela.entities.Calificacion;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

        CalificacionRequest request =
                new CalificacionRequest(15L, new BigDecimal("8.5"));

        assertThrows(
                ConflictoException.class,
                () -> service.registrar(request)
        );

        verify(calificacionRepository, never()).saveAndFlush(any());
    }

    @Test
    void registrarGuardaCalificacionParaInscripcionSinNota() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        when(inscripcionRepository.findById(15L)).thenReturn(Optional.of(inscripcion));
        when(calificacionRepository.existsByInscripcionId(15L)).thenReturn(false);
        when(calificacionMapper.requestAEntidad(
                any(CalificacionRequest.class), eq(inscripcion)
        )).thenReturn(Calificacion.crear(inscripcion, new BigDecimal("8.5")));
        when(calificacionRepository.saveAndFlush(any(Calificacion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.registrar(new CalificacionRequest(15L, new BigDecimal("8.5")));

        verify(calificacionRepository).saveAndFlush(any(Calificacion.class));
        org.junit.jupiter.api.Assertions.assertEquals(
                new BigDecimal("8.5"), inscripcion.getCalificacion().getCalificacion()
        );
    }

    @Test
    void actualizarRechazaInscripcionConOtraCalificacion() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        Calificacion existente = Calificacion.crear(inscripcion, new BigDecimal("7.0"));
        when(calificacionRepository.findById(2L)).thenReturn(Optional.of(existente));
        when(calificacionRepository.existsByInscripcionIdAndIdNot(15L, 2L)).thenReturn(true);

        CalificacionRequest request =
                new CalificacionRequest(15L, new BigDecimal("9.0"));

        assertThrows(
                ConflictoException.class,
                () -> service.actualizar(request, 2L)
        );

        verify(calificacionRepository, never()).saveAndFlush(any(Calificacion.class));
    }

    @Test
    void actualizarGuardaNotaCuandoInscripcionNoTieneOtraCalificacion() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        Calificacion existente = Calificacion.crear(inscripcion, new BigDecimal("7.0"));
        when(calificacionRepository.findById(2L)).thenReturn(Optional.of(existente));
        when(inscripcionRepository.findById(15L)).thenReturn(Optional.of(inscripcion));
        when(calificacionRepository.existsByInscripcionIdAndIdNot(15L, 2L)).thenReturn(false);
        when(calificacionRepository.saveAndFlush(existente)).thenReturn(existente);

        service.actualizar(new CalificacionRequest(15L, new BigDecimal("9.0")), 2L);

        org.junit.jupiter.api.Assertions.assertEquals(
                new BigDecimal("9.0"), existente.getCalificacion()
        );
        verify(calificacionRepository).saveAndFlush(existente);
    }

    @Test
    void eliminarQuitaLaRelacionYBorraCalificacion() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        Calificacion calificacion = Calificacion.crear(inscripcion, new BigDecimal("8.5"));
        when(calificacionRepository.findById(2L)).thenReturn(Optional.of(calificacion));

        service.eliminar(2L);

        assertNull(inscripcion.getCalificacion());
        verify(calificacionRepository).delete(calificacion);
        verify(calificacionRepository).flush();
    }

    @Test
    void listarYObtenerPorIdConsultanRepositorio() {
        Inscripcion inscripcion = Inscripcion.builder().id(15L).build();
        Calificacion calificacion = Calificacion.crear(inscripcion, new BigDecimal("8.5"));
        when(calificacionRepository.findAll()).thenReturn(List.of(calificacion));
        when(calificacionRepository.findById(2L)).thenReturn(Optional.of(calificacion));

        service.listar();
        service.obtenerPorId(2L);

        verify(calificacionMapper, times(2)).responseAEntidad(calificacion);
    }
}
