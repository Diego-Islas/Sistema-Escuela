package com.diego.escuela.controller;

import com.diego.escuela.services.aulas.AulaService;
import com.diego.escuela.dto.aulas.AulaRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class AulaControllerTest {
    @Mock
    private AulaService aulaService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AulaController(aulaService)).build();
    }

    @Test
    void getListaAulasDelegaAlServicioYDevuelveLista() throws Exception {
        when(aulaService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/aulas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(aulaService).listar();
    }

    @Test
    void getAulaPorIdUsaLaRutaDelModuloYDelegaAlServicio() throws Exception {
        when(aulaService.obtenerPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/aulas/1"))
                .andExpect(status().isOk());

        verify(aulaService).obtenerPorId(1L);
    }

    @Test
    void postAulaDelegaAlServicioYDevuelve201() throws Exception {
        mockMvc.perform(post("/api/aulas").contentType(APPLICATION_JSON)
                        .content("""
                                {"nombre":"Aula 202","capacidad":25}
                                """))
                .andExpect(status().isCreated());

        verify(aulaService).registrar(any(AulaRequest.class));
    }

    @Test
    void putAulaDelegaAlServicioYDevuelve200() throws Exception {
        mockMvc.perform(put("/api/aulas/1").contentType(APPLICATION_JSON)
                        .content("""
                                {"nombre":"Aula 202","capacidad":25}
                                """))
                .andExpect(status().isOk());

        verify(aulaService).actualizar(any(AulaRequest.class), eq(1L));
    }

    @Test
    void deleteAulaDelegaAlServicioYDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/aulas/1"))
                .andExpect(status().isNoContent());

        verify(aulaService).eliminar(1L);
    }
}
