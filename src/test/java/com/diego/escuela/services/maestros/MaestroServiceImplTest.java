package com.diego.escuela.services.maestros;

import com.diego.escuela.dto.maestros.MaestroRequest;
import com.diego.escuela.entities.Curso;
import com.diego.escuela.entities.Maestro;
import com.diego.escuela.exceptions.ConflictoException;
import com.diego.escuela.exceptions.EntidadRelacionadaException;
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
import static org.mockito.ArgumentMatchers.eq;
import java.util.Optional;
import java.util.List;

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

    @Test
    void registrarRechazaTelefonoExistente() {
        MaestroRequest request = new MaestroRequest(
                "Ana", "Pérez", "López", "ana@escuela.com", "5512345678"
        );
        when(maestroRepository.existsByEmail("ana@escuela.com")).thenReturn(false);
        when(maestroRepository.existsByTelefono("5512345678")).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.registrar(request));

        verify(maestroRepository, never()).saveAndFlush(any(Maestro.class));
    }

    @Test
    void registrarGuardaMaestroConEmailYTelefonoDisponibles() {
        MaestroRequest request = new MaestroRequest(
                "Ana", "Pérez", "López", "ana@escuela.com", "5512345678"
        );
        when(maestroRepository.existsByEmail("ana@escuela.com")).thenReturn(false);
        when(maestroRepository.existsByTelefono("5512345678")).thenReturn(false);

        service.registrar(request);

        verify(maestroRepository).saveAndFlush(any(Maestro.class));
        verify(maestroMapper).responseAEntidad(any(Maestro.class));
    }

    @Test
    void actualizarValidaUnicidadExcluyendoAlPropioMaestro() {
        Maestro maestro = Maestro.builder()
                .id(4L).nombre("Ana").apellidoPaterno("Pérez").apellidoMaterno("López")
                .email("ana@escuela.com").telefono("5512345678").build();
        MaestroRequest request = new MaestroRequest(
                "Ana", "Pérez", "López", "ana.nueva@escuela.com", "5598765432"
        );
        when(maestroRepository.findById(4L)).thenReturn(Optional.of(maestro));
        when(maestroRepository.existsByEmailAndIdNot("ana.nueva@escuela.com", 4L)).thenReturn(false);
        when(maestroRepository.existsByTelefonoAndIdNot("5598765432", 4L)).thenReturn(false);

        service.actualizar(request, 4L);

        verify(maestroRepository).existsByEmailAndIdNot("ana.nueva@escuela.com", 4L);
        verify(maestroRepository).existsByTelefonoAndIdNot("5598765432", 4L);
        verify(maestroRepository).saveAndFlush(maestro);
    }

    @Test
    void actualizarRechazaEmailUsadoPorOtroMaestro() {
        Maestro maestro = Maestro.builder()
                .id(4L).nombre("Ana").apellidoPaterno("Pérez").apellidoMaterno("López")
                .email("ana@escuela.com").telefono("5512345678").build();
        MaestroRequest request = new MaestroRequest(
                "Ana", "Pérez", "López", "otro@escuela.com", "5598765432"
        );
        when(maestroRepository.findById(4L)).thenReturn(Optional.of(maestro));
        when(maestroRepository.existsByEmailAndIdNot("otro@escuela.com", 4L)).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.actualizar(request, 4L));

        verify(maestroRepository, never()).saveAndFlush(any(Maestro.class));
    }

    @Test
    void actualizarRechazaTelefonoUsadoPorOtroMaestro() {
        Maestro maestro = Maestro.builder()
                .id(4L).nombre("Ana").apellidoPaterno("Pérez").apellidoMaterno("López")
                .email("ana@escuela.com").telefono("5512345678").build();
        MaestroRequest request = new MaestroRequest(
                "Ana", "Pérez", "López", "otro@escuela.com", "5598765432"
        );
        when(maestroRepository.findById(4L)).thenReturn(Optional.of(maestro));
        when(maestroRepository.existsByEmailAndIdNot("otro@escuela.com", 4L)).thenReturn(false);
        when(maestroRepository.existsByTelefonoAndIdNot("5598765432", 4L)).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.actualizar(request, 4L));

        verify(maestroRepository, never()).saveAndFlush(any(Maestro.class));
    }

    @Test
    void eliminarRechazaMaestroConGrupos() {
        Maestro maestro = Maestro.builder().id(4L).build();
        when(maestroRepository.findById(4L)).thenReturn(Optional.of(maestro));
        when(grupoRepository.existsByMaestroId(4L)).thenReturn(true);

        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(4L));

        verify(maestroRepository, never()).delete(maestro);
    }

    @Test
    void eliminarBorraMaestroSinGrupos() {
        Maestro maestro = Maestro.builder().id(4L).build();
        when(maestroRepository.findById(4L)).thenReturn(Optional.of(maestro));
        when(grupoRepository.existsByMaestroId(4L)).thenReturn(false);

        service.eliminar(4L);

        verify(maestroRepository).delete(maestro);
        verify(maestroRepository).flush();
    }

    @Test
    void obtenerCursosValidaMaestroYTransformaResultados() {
        Maestro maestro = Maestro.builder().id(4L).build();
        Curso curso = Curso.builder().id(2L).build();
        when(maestroRepository.findById(4L)).thenReturn(Optional.of(maestro));
        when(cursoRepository.obtenerCursosPorIdMaestro(4L)).thenReturn(List.of(curso));
        when(cursoMapper.entidadADatosCurso(curso))
                .thenReturn(new com.diego.escuela.dto.datos.DatosCurso("Matemáticas I", "Fundamentos", 6));

        var resultado = service.obtenerCursosDeUnMaestroConId(4L);

        org.junit.jupiter.api.Assertions.assertEquals(1, resultado.size());
        verify(cursoRepository).obtenerCursosPorIdMaestro(eq(4L));
    }

    @Test
    void listarYObtenerPorIdConsultanRepositorio() {
        Maestro maestro = Maestro.builder().id(4L).build();
        when(maestroRepository.findAll()).thenReturn(List.of(maestro));
        when(maestroRepository.findById(4L)).thenReturn(Optional.of(maestro));

        service.listar();
        service.obtenerPorId(4L);

        verify(maestroMapper, org.mockito.Mockito.times(2)).responseAEntidad(maestro);
    }
}
