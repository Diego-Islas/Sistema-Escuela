package com.diego.escuela.services.cursos;

import com.diego.escuela.dto.cursos.CursoRequest;
import com.diego.escuela.entities.Curso;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.mapper.CursoMapper;
import com.diego.escuela.repositories.CursoRepository;
import com.diego.escuela.repositories.GrupoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CursoServiceImplTest {
    @Mock
    private CursoRepository cursoRepository;
    @Mock
    private CursoMapper cursoMapper;
    @Mock
    private GrupoRepository grupoRepository;

    @InjectMocks
    private CursoServiceImpl service;

    @Test
    void registrarRechazaNombreDuplicado() {
        CursoRequest request = new CursoRequest("Matemáticas I", "Fundamentos", 6);
        Curso curso = Curso.crear(request.nombre(), request.descripcion(), request.creditos());
        when(cursoMapper.requestAEntidad(request)).thenReturn(curso);
        when(cursoRepository.existsByNombre("Matemáticas I")).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.registrar(request));

        verify(cursoRepository, never()).save(curso);
    }
}
