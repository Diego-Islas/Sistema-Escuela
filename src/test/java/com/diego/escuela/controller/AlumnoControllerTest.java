package com.diego.escuela.controller;

import com.diego.escuela.dto.alumnos.AlumnoRequest;
import com.diego.escuela.dto.alumnos.AlumnoResponse;
import com.diego.escuela.services.alumnos.AlumnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AlumnoControllerTest {
    @Mock
    private AlumnoService alumnoService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AlumnoController(alumnoService)).build();
    }

    @Test
    void getListaAlumnosYDevuelveListaVacia() throws Exception {
        when(alumnoService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/alumnos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(alumnoService).listar();
    }

    @Test
    void postRegistraAlumnoYDevuelve201() throws Exception {
        AlumnoResponse response = respuestaAlumno();
        when(alumnoService.registrar(any(AlumnoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/alumnos")
                        .contentType("application/json")
                        .content("""
                                {
                                  "nombre": "Carlos",
                                  "apellidoPaterno": "González",
                                  "apellidoMaterno": "Ramírez"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.matricula").value("GORACA2601"))
                .andExpect(jsonPath("$.promedio").value(0.00));

        verify(alumnoService).registrar(any(AlumnoRequest.class));
    }

    @Test
    void getAlumnoPorIdDevuelve200() throws Exception {
        when(alumnoService.obtenerPorId(1L)).thenReturn(respuestaAlumno());

        mockMvc.perform(get("/api/alumnos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carlos González Ramírez"));

        verify(alumnoService).obtenerPorId(1L);
    }

    @Test
    void putActualizaAlumnoYDevuelve200() throws Exception {
        when(alumnoService.actualizar(any(AlumnoRequest.class), eq(1L)))
                .thenReturn(respuestaAlumno());

        mockMvc.perform(put("/api/alumnos/1")
                        .contentType("application/json")
                        .content("""
                                {
                                  "nombre": "Carlos",
                                  "apellidoPaterno": "González",
                                  "apellidoMaterno": "Ramírez"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(alumnoService).actualizar(any(AlumnoRequest.class), eq(1L));
    }

    @Test
    void deleteEliminaAlumnoYDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/alumnos/1"))
                .andExpect(status().isNoContent());

        verify(alumnoService).eliminar(1L);
    }

    @Test
    void postRechazaRequestConDatosPersonalesVacios() throws Exception {
        mockMvc.perform(post("/api/alumnos")
                        .contentType("application/json")
                        .content("""
                                {
                                  "nombre": "",
                                  "apellidoPaterno": "Pérez",
                                  "apellidoMaterno": "López"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(alumnoService);
    }

    @Test
    void getRechazaIdentificadorNoPositivo() throws Exception {
        mockMvc.perform(get("/api/alumnos/0"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(alumnoService);
    }

    private AlumnoResponse respuestaAlumno() {
        return new AlumnoResponse(
                1L,
                "Carlos González Ramírez",
                "atr.26.carlos.gonzalez.ramirez.goraca2601@escuela.com.mx",
                "GORACA2601",
                "10/01/2025",
                List.of(),
                new BigDecimal("0.00")
        );
    }
}
