package com.diego.escuela.services.maestros;

import com.diego.escuela.dto.datos.DatosCurso;
import com.diego.escuela.dto.maestros.MaestroRequest;
import com.diego.escuela.dto.maestros.MaestroResponse;
import com.diego.escuela.services.CrudService;

import java.util.List;

public interface MaestroService extends CrudService<MaestroRequest, MaestroResponse> {
    List<DatosCurso> obtenerCursosDeUnMaestroConId(Long id);
}