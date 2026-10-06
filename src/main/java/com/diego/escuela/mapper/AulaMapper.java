package com.diego.escuela.mapper;

import com.diego.escuela.dto.aulas.AulaRequest;
import com.diego.escuela.dto.aulas.AulaResponse;
import com.diego.escuela.entities.Aula;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper implements CommonMapper<AulaRequest, AulaResponse, Aula> {
    @Override
    public Aula requestAEntidad(AulaRequest request) {
        return request == null ? null : Aula.crear(request.nombre(), request.capacidad());
    }

    @Override
    public AulaResponse responseAEntidad(Aula entidad) {
        return entidad == null ? null :
                new AulaResponse(entidad.getId(), entidad.getNombre(), entidad.getCapacidad());
    }
}
