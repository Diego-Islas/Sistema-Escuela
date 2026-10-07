package com.diego.escuela.controller;

import com.diego.escuela.services.grupos.GrupoService;
import com.diego.escuela.dto.grupos.GrupoRequest;
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
class GrupoControllerTest {
    @Mock
    private GrupoService grupoService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GrupoController(grupoService)).build();
    }

    @Test
    void getListaGruposDelegaAlServicioYDevuelveLista() throws Exception {
        when(grupoService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/grupos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(grupoService).listar();
    }

    @Test
    void getGrupoPorIdUsaLaRutaDelModuloYDelegaAlServicio() throws Exception {
        when(grupoService.obtenerPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/grupos/1"))
                .andExpect(status().isOk());

        verify(grupoService).obtenerPorId(1L);
    }

    @Test
    void postGrupoDelegaAlServicioYDevuelve201() throws Exception {
        mockMvc.perform(post("/api/grupos").contentType(APPLICATION_JSON)
                        .content("""
                                {"idCurso":2,"idMaestro":7,"idAula":3,"periodo":"2026-01"}
                                """))
                .andExpect(status().isCreated());

        verify(grupoService).registrar(any(GrupoRequest.class));
    }

    @Test
    void putGrupoDelegaAlServicioYDevuelve200() throws Exception {
        mockMvc.perform(put("/api/grupos/1").contentType(APPLICATION_JSON)
                        .content("""
                                {"idCurso":2,"idMaestro":7,"idAula":3,"periodo":"2026-01"}
                                """))
                .andExpect(status().isOk());

        verify(grupoService).actualizar(any(GrupoRequest.class), eq(1L));
    }

    @Test
    void deleteGrupoDelegaAlServicioYDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/grupos/1"))
                .andExpect(status().isNoContent());

        verify(grupoService).eliminar(1L);
    }
}
