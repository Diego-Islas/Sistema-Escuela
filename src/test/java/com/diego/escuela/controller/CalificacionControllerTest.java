package com.diego.escuela.controller;

import com.diego.escuela.services.calificaciones.CalificacionService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
