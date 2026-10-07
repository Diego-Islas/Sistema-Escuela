package com.diego.escuela.services.aulas;

import com.diego.escuela.entities.Aula;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
