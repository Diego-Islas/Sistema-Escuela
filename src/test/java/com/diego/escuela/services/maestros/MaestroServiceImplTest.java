package com.diego.escuela.services.maestros;

import com.diego.escuela.dto.maestros.MaestroRequest;
import com.diego.escuela.entities.Maestro;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.mapper.CursoMapper;
import com.diego.escuela.mapper.MaestroMapper;
import com.diego.escuela.repositories.CursoRepository;
import com.diego.escuela.repositories.GrupoRepository;
import com.diego.escuela.repositories.MaestroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaestroServiceImplTest {
    @Mock
    private MaestroRepository maestroRepository;
    @Mock
    private MaestroMapper maestroMapper;
    @Mock
    private CursoRepository cursoRepository;
    @Mock
    private CursoMapper cursoMapper;
    @Mock
    private GrupoRepository grupoRepository;

    @InjectMocks
    private MaestroServiceImpl service;

    @Test
    void registrarRechazaEmailExistente() {
        MaestroRequest request = new MaestroRequest(
                "Ana", "Pérez", "López", "ana@escuela.com", "5512345678"
        );
        when(maestroRepository.existsByEmail("ana@escuela.com")).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.registrar(request));

        verify(maestroRepository, never()).save(any(Maestro.class));
    }
}
