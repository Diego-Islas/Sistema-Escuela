package com.diego.escuela.controller;

import com.diego.escuela.services.calificaciones.CalificacionService;
import com.diego.escuela.dto.calificaciones.CalificacionRequest;
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
class CalificacionControllerTest {
    @Mock
    private CalificacionService calificacionService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CalificacionController(calificacionService)).build();
    }

    @Test
    void getListaCalificacionesDelegaAlServicioYDevuelveLista() throws Exception {
        when(calificacionService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/calificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(calificacionService).listar();
    }

    @Test
    void getCalificacionPorIdUsaLaRutaDelModuloYDelegaAlServicio() throws Exception {
        when(calificacionService.obtenerPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/calificaciones/1"))
                .andExpect(status().isOk());

        verify(calificacionService).obtenerPorId(1L);
    }

    @Test
    void postCalificacionDelegaAlServicioYDevuelve201() throws Exception {
        mockMvc.perform(post("/api/calificaciones").contentType(APPLICATION_JSON)
                        .content("""
                                {"idInscripcion":15,"calificacion":8.5}
                                """))
                .andExpect(status().isCreated());

        verify(calificacionService).registrar(any(CalificacionRequest.class));
    }

    @Test
    void putCalificacionDelegaAlServicioYDevuelve200() throws Exception {
        mockMvc.perform(put("/api/calificaciones/2").contentType(APPLICATION_JSON)
                        .content("""
                                {"idInscripcion":15,"calificacion":9.0}
                                """))
                .andExpect(status().isOk());

        verify(calificacionService).actualizar(any(CalificacionRequest.class), eq(2L));
    }

    @Test
    void deleteCalificacionDelegaAlServicioYDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/calificaciones/2"))
                .andExpect(status().isNoContent());

        verify(calificacionService).eliminar(2L);
    }
}
