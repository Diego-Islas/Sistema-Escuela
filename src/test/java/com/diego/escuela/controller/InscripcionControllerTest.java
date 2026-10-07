package com.diego.escuela.controller;

import com.diego.escuela.services.inscripciones.InscripcionService;
import com.diego.escuela.dto.inscripciones.InscripcionRequest;
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
class InscripcionControllerTest {
    @Mock
    private InscripcionService inscripcionService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new InscripcionController(inscripcionService)).build();
    }

    @Test
    void getListaInscripcionesDelegaAlServicioYDevuelveLista() throws Exception {
        when(inscripcionService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/inscripciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(inscripcionService).listar();
    }

    @Test
    void getInscripcionPorIdUsaLaRutaDelModuloYDelegaAlServicio() throws Exception {
        when(inscripcionService.obtenerPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/inscripciones/1"))
                .andExpect(status().isOk());

        verify(inscripcionService).obtenerPorId(1L);
    }

    @Test
    void postInscripcionDelegaAlServicioYDevuelve201() throws Exception {
        mockMvc.perform(post("/api/inscripciones").contentType(APPLICATION_JSON)
                        .content("""
                                {"idAlumno":10,"idGrupo":5}
                                """))
                .andExpect(status().isCreated());

        verify(inscripcionService).registrar(any(InscripcionRequest.class));
    }

    @Test
    void putInscripcionDelegaAlServicioYDevuelve200() throws Exception {
        mockMvc.perform(put("/api/inscripciones/15").contentType(APPLICATION_JSON)
                        .content("""
                                {"idAlumno":10,"idGrupo":5}
                                """))
                .andExpect(status().isOk());

        verify(inscripcionService).actualizar(any(InscripcionRequest.class), eq(15L));
    }

    @Test
    void deleteInscripcionDelegaAlServicioYDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/inscripciones/15"))
                .andExpect(status().isNoContent());

        verify(inscripcionService).eliminar(15L);
    }
}
