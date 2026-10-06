package com.diego.escuela.mapper;

public interface CommonMapper<RQ, RS, E> {
    E requestAEntidad(RQ request);

    RS responseAEntidad(E entidad);
}
