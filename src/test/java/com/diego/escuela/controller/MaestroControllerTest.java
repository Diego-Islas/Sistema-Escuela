package com.diego.escuela.controller;

import com.diego.escuela.dto.datos.DatosCurso;
import com.diego.escuela.services.maestros.MaestroService;
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
class MaestroControllerTest {
    @Mock
    private MaestroService maestroService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MaestroController(maestroService)).build();
    }

    @Test
    void getCursosDelMaestroDevuelveCursos() throws Exception {
        when(maestroService.obtenerCursosDeUnMaestroConId(1L)).thenReturn(List.of(
                new DatosCurso("Matemáticas I", "Fundamentos", 6)
        ));

        mockMvc.perform(get("/api/maestros/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Matemáticas I"))
                .andExpect(jsonPath("$[0].creditos").value(6));

        verify(maestroService).obtenerCursosDeUnMaestroConId(1L);
    }
}
