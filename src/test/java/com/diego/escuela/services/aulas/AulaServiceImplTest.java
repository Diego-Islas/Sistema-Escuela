package com.diego.escuela.services.aulas;

import com.diego.escuela.dto.aulas.AulaRequest;
import com.diego.escuela.dto.aulas.AulaResponse;
import com.diego.escuela.entities.Aula;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.AulaMapper;
import com.diego.escuela.repositories.AulaRepository;
import com.diego.escuela.repositories.GrupoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AulaServiceImplTest {
    @Mock
    private AulaRepository aulaRepository;
    @Mock
    private AulaMapper aulaMapper;
    @Mock
    private GrupoRepository grupoRepository;

    @InjectMocks
    private AulaServiceImpl service;

    @Test
    void eliminarRechazaAulaConGruposAsignados() {
        Aula aula = Aula.builder().id(3L).nombre("Aula 101").capacidad(30).build();
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(aula));
        when(grupoRepository.existsByAulaId(3L)).thenReturn(true);

        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(3L));

        verify(aulaRepository, never()).delete(aula);
    }

    @Test
    void registrarGuardaAulaConNombreDisponible() {
        AulaRequest request = new AulaRequest("Aula 101", 30);
        Aula aula = Aula.crear(request.nombre(), request.capacidad());
        AulaResponse response = new AulaResponse(1L, "Aula 101", 30);
        when(aulaMapper.requestAEntidad(request)).thenReturn(aula);
        when(aulaRepository.existsByNombre("Aula 101")).thenReturn(false);
        when(aulaRepository.saveAndFlush(aula)).thenReturn(aula);
        when(aulaMapper.responseAEntidad(aula)).thenReturn(response);

        AulaResponse resultado = service.registrar(request);

        org.junit.jupiter.api.Assertions.assertEquals(response, resultado);
        verify(aulaRepository).saveAndFlush(aula);
    }

    @Test
    void actualizarRechazaNombreUsadoPorOtraAula() {
        Aula actual = Aula.builder().id(3L).nombre("Aula 101").capacidad(30).build();
        AulaRequest request = new AulaRequest("Aula 202", 25);
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(actual));
        when(aulaMapper.requestAEntidad(request)).thenReturn(Aula.crear("Aula 202", 25));
        when(aulaRepository.existsByNombreAndIdNot("Aula 202", 3L)).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.actualizar(request, 3L));

        verify(aulaRepository, never()).saveAndFlush(any(Aula.class));
    }

    @Test
    void actualizarGuardaCambiosCuandoElNombreEstaDisponible() {
        Aula actual = Aula.builder().id(3L).nombre("Aula 101").capacidad(30).build();
        AulaRequest request = new AulaRequest("Aula 202", 25);
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(actual));
        when(aulaMapper.requestAEntidad(request)).thenReturn(Aula.crear("Aula 202", 25));
        when(aulaRepository.existsByNombreAndIdNot("Aula 202", 3L)).thenReturn(false);
        when(aulaRepository.saveAndFlush(actual)).thenReturn(actual);

        service.actualizar(request, 3L);

        org.junit.jupiter.api.Assertions.assertEquals("Aula 202", actual.getNombre());
        org.junit.jupiter.api.Assertions.assertEquals(25, actual.getCapacidad());
        verify(aulaRepository).saveAndFlush(actual);
    }

    @Test
    void eliminarBorraAulaSinGrupos() {
        Aula aula = Aula.builder().id(3L).nombre("Aula 101").capacidad(30).build();
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(aula));
        when(grupoRepository.existsByAulaId(3L)).thenReturn(false);

        service.eliminar(3L);

        verify(aulaRepository).delete(aula);
        verify(aulaRepository).flush();
    }

    @Test
    void listarYObtenerPorIdConsultanRepositorio() {
        Aula aula = Aula.builder().id(3L).nombre("Aula 101").capacidad(30).build();
        when(aulaRepository.findAll()).thenReturn(List.of(aula));
        when(aulaRepository.findById(3L)).thenReturn(Optional.of(aula));

        service.listar();
        service.obtenerPorId(3L);

        verify(aulaMapper, org.mockito.Mockito.times(2)).responseAEntidad(aula);
    }
}
