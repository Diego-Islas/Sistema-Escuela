package com.diego.escuela.repositories;

import com.diego.escuela.entities.Calificacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    boolean existsByInscripcionId(Long idInscripcion);

    boolean existsByInscripcionIdAndIdNot(
            Long idInscripcion,
            Long idCalificacion
    );

    @Override
    @EntityGraph(attributePaths = {
            "inscripcion.alumno",
            "inscripcion.grupo.curso",
            "inscripcion.grupo.maestro",
            "inscripcion.grupo.aula"
    })
    List<Calificacion> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "inscripcion.alumno",
            "inscripcion.grupo.curso",
            "inscripcion.grupo.maestro",
            "inscripcion.grupo.aula"
    })
    Optional<Calificacion> findById(Long id);
}