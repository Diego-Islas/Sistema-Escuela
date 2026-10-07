package com.diego.escuela.controller;

import com.diego.escuela.services.horarios.HorarioService;
import com.diego.escuela.dto.horarios.HorarioRequest;
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
class HorarioControllerTest {
    @Mock
    private HorarioService horarioService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HorarioController(horarioService)).build();
    }

    @Test
    void getListaHorariosDelegaAlServicioYDevuelveLista() throws Exception {
        when(horarioService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/horarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(horarioService).listar();
    }

    @Test
    void getHorarioPorIdUsaLaRutaDelModuloYDelegaAlServicio() throws Exception {
        when(horarioService.obtenerPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/horarios/1"))
                .andExpect(status().isOk());

        verify(horarioService).obtenerPorId(1L);
    }

    @Test
    void postHorarioDelegaAlServicioYDevuelve201() throws Exception {
        mockMvc.perform(post("/api/horarios").contentType(APPLICATION_JSON)
                        .content("""
                                {"idGrupo":1,"dia":"Lunes","horaInicio":"08:00","horaFin":"10:00"}
                                """))
                .andExpect(status().isCreated());

        verify(horarioService).registrar(any(HorarioRequest.class));
    }

    @Test
    void putHorarioDelegaAlServicioYDevuelve200() throws Exception {
        mockMvc.perform(put("/api/horarios/8").contentType(APPLICATION_JSON)
                        .content("""
                                {"idGrupo":1,"dia":"Martes","horaInicio":"10:00","horaFin":"12:00"}
                                """))
                .andExpect(status().isOk());

        verify(horarioService).actualizar(any(HorarioRequest.class), eq(8L));
    }

    @Test
    void deleteHorarioDelegaAlServicioYDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/horarios/8"))
                .andExpect(status().isNoContent());

        verify(horarioService).eliminar(8L);
    }
}
