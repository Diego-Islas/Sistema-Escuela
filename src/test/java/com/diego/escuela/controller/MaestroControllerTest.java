package com.diego.escuela.controller;

import com.diego.escuela.dto.maestros.MaestroRequest;
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

    @Test
    void getListaMaestrosDelegaAlServicioYDevuelveLista() throws Exception {
        when(maestroService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/maestros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(maestroService).listar();
    }

    @Test
    void getMaestroPorIdUsaLaRutaDelModuloYDelegaAlServicio() throws Exception {
        when(maestroService.obtenerPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/maestros/1"))
                .andExpect(status().isOk());

        verify(maestroService).obtenerPorId(1L);
    }

    @Test
    void postMaestroDelegaAlServicioYDevuelve201() throws Exception {
        mockMvc.perform(post("/api/maestros").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre":"Ana",
                                  "apellidoPaterno":"Pérez",
                                  "apellidoMaterno":"López",
                                  "email":"ana@escuela.com",
                                  "telefono":"5512345678"
                                }
                                """))
                .andExpect(status().isCreated());

        verify(maestroService).registrar(any(MaestroRequest.class));
    }

    @Test
    void putMaestroDelegaAlServicioYDevuelve200() throws Exception {
        mockMvc.perform(put("/api/maestros/1").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre":"Ana",
                                  "apellidoPaterno":"Pérez",
                                  "apellidoMaterno":"López",
                                  "email":"ana@escuela.com",
                                  "telefono":"5512345678"
                                }
                                """))
                .andExpect(status().isOk());

        verify(maestroService).actualizar(any(MaestroRequest.class), eq(1L));
    }

    @Test
    void deleteMaestroDelegaAlServicioYDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/maestros/1"))
                .andExpect(status().isNoContent());

        verify(maestroService).eliminar(1L);
    }

    @Test
    void getCursosRechazaIdentificadorNoPositivo() throws Exception {
        mockMvc.perform(get("/api/maestros/cursos/0"))
                .andExpect(status().isBadRequest());

        org.mockito.Mockito.verifyNoInteractions(maestroService);
    }
}
