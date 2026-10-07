package com.diego.escuela.services.horarios;

import com.diego.escuela.dto.horarios.HorarioRequest;
import com.diego.escuela.entities.Aula;
import com.diego.escuela.entities.Grupo;
import com.diego.escuela.enums.DiaSemana;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.mapper.HorarioMapper;
import com.diego.escuela.repositories.GrupoRepository;
import com.diego.escuela.repositories.HorarioRepository;
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
        Grupo grupo = Grupo.builder()
                .id(1L)
                .aula(Aula.builder().id(3L).nombre("Aula 101").capacidad(30).build())
                .build();
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(grupo));
        when(horarioRepository.existeTraslape(
                DiaSemana.LUNES, 1L, 3L, "08:00", "10:00"
        )).thenReturn(true);
        HorarioRequest request = new HorarioRequest(1L, "Lunes", "08:00", "10:00");

        assertThrows(ConflictoException.class, () -> service.registrar(request));

        verify(horarioRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
