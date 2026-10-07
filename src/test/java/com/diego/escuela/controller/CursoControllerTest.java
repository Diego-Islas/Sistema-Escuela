package com.diego.escuela.controller;

import com.diego.escuela.services.cursos.CursoService;
import com.diego.escuela.dto.cursos.CursoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CursoControllerTest {
    @Mock
    private CursoService cursoService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CursoController(cursoService)).build();
    }

    @Test
    void getListaCursosDelegaAlServicioYDevuelveLista() throws Exception {
        when(cursoService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(cursoService).listar();
    }

    @Test
    void getCursoPorIdUsaLaRutaDelModuloYDelegaAlServicio() throws Exception {
        when(cursoService.obtenerPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk());

        verify(cursoService).obtenerPorId(1L);
    }

    @Test
    void postCursoDelegaAlServicioYDevuelve201() throws Exception {
        mockMvc.perform(post("/api/cursos").contentType(APPLICATION_JSON)
                        .content("""
                                {"nombre":"Bases de Datos","descripcion":"Introducción","creditos":5}
                                """))
                .andExpect(status().isCreated());

        verify(cursoService).registrar(any(CursoRequest.class));
    }

    @Test
    void putCursoDelegaAlServicioYDevuelve200() throws Exception {
        mockMvc.perform(put("/api/cursos/2").contentType(APPLICATION_JSON)
                        .content("""
                                {"nombre":"Bases de Datos","descripcion":"Introducción","creditos":5}
                                """))
                .andExpect(status().isOk());

        verify(cursoService).actualizar(any(CursoRequest.class), eq(2L));
    }

    @Test
    void deleteCursoDelegaAlServicioYDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/cursos/2"))
                .andExpect(status().isNoContent());

        verify(cursoService).eliminar(2L);
    }
}
