package com.diego.escuela.services.cursos;

import com.diego.escuela.dto.cursos.CursoRequest;
import com.diego.escuela.dto.cursos.CursoResponse;
import com.diego.escuela.entities.Curso;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
import com.diego.escuela.mapper.CursoMapper;
import com.diego.escuela.repositories.CursoRepository;
import com.diego.escuela.repositories.GrupoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

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

    @Test
    void registrarGuardaCursoConNombreDisponible() {
        CursoRequest request = new CursoRequest("Matemáticas I", "Fundamentos", 6);
        Curso curso = Curso.crear(request.nombre(), request.descripcion(), request.creditos());
        CursoResponse response = new CursoResponse(2L, "Matemáticas I", "Fundamentos", 6);
        when(cursoMapper.requestAEntidad(request)).thenReturn(curso);
        when(cursoRepository.existsByNombre("Matemáticas I")).thenReturn(false);
        when(cursoRepository.saveAndFlush(curso)).thenReturn(curso);
        when(cursoMapper.responseAEntidad(curso)).thenReturn(response);

        org.junit.jupiter.api.Assertions.assertEquals(response, service.registrar(request));
        verify(cursoRepository).saveAndFlush(curso);
    }

    @Test
    void actualizarRechazaNombreDuplicadoEnOtroCurso() {
        Curso actual = Curso.builder().id(2L).nombre("Matemáticas I")
                .descripcion("Fundamentos").creditos(6).build();
        CursoRequest request = new CursoRequest("Bases de Datos", "Introducción", 5);
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(actual));
        when(cursoMapper.requestAEntidad(request)).thenReturn(
                Curso.crear("Bases de Datos", "Introducción", 5)
        );
        when(cursoRepository.existsByNombreAndIdNot("Bases de Datos", 2L)).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.actualizar(request, 2L));

        verify(cursoRepository, never()).saveAndFlush(any(Curso.class));
    }

    @Test
    void actualizarGuardaCambiosCuandoElNombreEstaDisponible() {
        Curso actual = Curso.builder().id(2L).nombre("Matemáticas I")
                .descripcion("Fundamentos").creditos(6).build();
        CursoRequest request = new CursoRequest("Bases de Datos", "Introducción", 5);
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(actual));
        when(cursoMapper.requestAEntidad(request)).thenReturn(
                Curso.crear("Bases de Datos", "Introducción", 5)
        );
        when(cursoRepository.existsByNombreAndIdNot("Bases de Datos", 2L)).thenReturn(false);
        when(cursoRepository.saveAndFlush(actual)).thenReturn(actual);

        service.actualizar(request, 2L);

        org.junit.jupiter.api.Assertions.assertEquals("Bases de Datos", actual.getNombre());
        org.junit.jupiter.api.Assertions.assertEquals("Introducción", actual.getDescripcion());
        org.junit.jupiter.api.Assertions.assertEquals(5, actual.getCreditos());
        verify(cursoRepository).saveAndFlush(actual);
    }

    @Test
    void eliminarRechazaCursoConGrupos() {
        Curso curso = Curso.builder().id(2L).nombre("Matemáticas I").build();
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso));
        when(grupoRepository.existsByCursoId(2L)).thenReturn(true);

        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(2L));

        verify(cursoRepository, never()).delete(curso);
    }

    @Test
    void eliminarBorraCursoSinGrupos() {
        Curso curso = Curso.builder().id(2L).nombre("Matemáticas I").build();
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso));
        when(grupoRepository.existsByCursoId(2L)).thenReturn(false);

        service.eliminar(2L);

        verify(cursoRepository).delete(curso);
        verify(cursoRepository).flush();
    }

    @Test
    void listarYObtenerPorIdConsultanRepositorio() {
        Curso curso = Curso.builder().id(2L).build();
        when(cursoRepository.findAll()).thenReturn(List.of(curso));
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso));

        service.listar();
        service.obtenerPorId(2L);

        verify(cursoMapper, times(2)).responseAEntidad(curso);
    }
}
