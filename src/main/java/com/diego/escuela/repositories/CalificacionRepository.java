package com.diego.escuela.repositories;

import com.diego.escuela.entities.Calificacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    @Query("""
            SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
            FROM Calificacion c
            WHERE c.inscripcion.id = :idInscripcion
            """)
    boolean existePorInscripcion(@Param("idInscripcion") Long idInscripcion);

    @Query("""
            SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
            FROM Calificacion c
            WHERE c.inscripcion.id = :idInscripcion
              AND c.id <> :idCalificacion
            """)
    boolean existePorInscripcionExcluyendo(
            @Param("idInscripcion") Long idInscripcion,
            @Param("idCalificacion") Long idCalificacion
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
